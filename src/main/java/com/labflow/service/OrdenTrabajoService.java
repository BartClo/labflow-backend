package com.labflow.service;

import com.labflow.dto.OrdenTrabajoDTO;
import com.labflow.dto.TareaBasicDTO;
import com.labflow.dto.UsuarioBasicDTO;
import com.labflow.dto.request.OrdenTrabajoCreateDTO;
import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.*;
import com.labflow.repository.MuestraAnalisisRepository;
import com.labflow.repository.OrdenTrabajoRepository;
import com.labflow.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar Órdenes de Trabajo
 */
@Service
@Transactional
public class OrdenTrabajoService {

    private static final Logger logger = LoggerFactory.getLogger(OrdenTrabajoService.class);

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private MuestraAnalisisRepository muestraAnalisisRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Genera automáticamente un código OT secuencial del formato OT-YYYY-NNNN
     */
    private String generarCodigoOT() {
        int year = java.time.LocalDateTime.now().getYear();
        String prefix = "OT-" + year + "-";
        
        // Buscar la última orden del año actual con límite de 1 resultado
        List<OrdenTrabajo> ordenesEncontradas = 
            ordenTrabajoRepository.findFirstByCodigoOTStartingWithOrderByCodigoOTDesc(prefix, PageRequest.of(0, 1));
        
        int siguienteNumero = 1;
        if (!ordenesEncontradas.isEmpty()) {
            String ultimoCodigo = ordenesEncontradas.get(0).getCodigoOT();
            // Extraer el número de la última parte (OT-2026-0001 -> 0001)
            String ultimoNumeroStr = ultimoCodigo.substring(ultimoCodigo.lastIndexOf('-') + 1);
            try {
                int ultimoNumero = Integer.parseInt(ultimoNumeroStr);
                siguienteNumero = ultimoNumero + 1;
            } catch (NumberFormatException e) {
                logger.warn("No se pudo parsear el número de la última OT: {}", ultimoCodigo);
            }
        }
        
        // Formatear con 4 dígitos: OT-2026-0001
        return prefix + String.format("%04d", siguienteNumero);
    }

    /**
     * Crea una nueva orden de trabajo agrupando tareas pendientes
     */
    public OrdenTrabajoDTO crearOrdenTrabajo(OrdenTrabajoCreateDTO dto) {
        logger.info("Creando orden de trabajo con {} tareas", dto.getTareaIds().size());

        // Validar que se proporcionaron tareas
        if (dto.getTareaIds() == null || dto.getTareaIds().isEmpty()) {
            throw new ValidationException("Debe proporcionar al menos una tarea para crear una orden de trabajo");
        }

        // Buscar todas las tareas
        List<MuestraAnalisis> tareas = new ArrayList<>();
        for (UUID tareaId : dto.getTareaIds()) {
            MuestraAnalisis tarea = muestraAnalisisRepository.findById(tareaId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Tarea no encontrada con ID: " + tareaId));
            tareas.add(tarea);
        }

        // Validar que todas las tareas estén en estado PENDIENTE
        List<MuestraAnalisis> tareasNoPendientes = tareas.stream()
                .filter(t -> t.getEstadoAnalisis() != MuestraAnalisis.EstadoAnalisis.PENDIENTE)
                .collect(Collectors.toList());

        if (!tareasNoPendientes.isEmpty()) {
            throw new ValidationException(
                    String.format("Hay %d tareas que no están en estado PENDIENTE. " +
                            "Solo se pueden asignar tareas pendientes a una orden de trabajo.",
                            tareasNoPendientes.size()));
        }

        // Validar que ninguna tarea ya esté asignada a otra orden
        List<MuestraAnalisis> tareasYaAsignadas = tareas.stream()
                .filter(t -> t.getOrdenTrabajo() != null)
                .collect(Collectors.toList());

        if (!tareasYaAsignadas.isEmpty()) {
            throw new ValidationException(
                    String.format("Hay %d tareas que ya están asignadas a otra orden de trabajo.",
                            tareasYaAsignadas.size()));
        }

        // Buscar técnico asignado
        Usuario tecnico = usuarioRepository.findById(dto.getTecnicoAsignadoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con ID: " + dto.getTecnicoAsignadoId()));

        // Crear la orden de trabajo
        OrdenTrabajo ordenTrabajo = new OrdenTrabajo();
        ordenTrabajo.setCodigoOT(generarCodigoOT());
        ordenTrabajo.setTecnicoAsignado(tecnico);
        ordenTrabajo.setEstado(EstadoOT.ABIERTA);

        // Guardar la orden de trabajo
        ordenTrabajo = ordenTrabajoRepository.save(ordenTrabajo);

        // Asignar la orden a cada tarea y cambiar su estado a EN_PROCESO
        final OrdenTrabajo ordenFinal = ordenTrabajo;
        tareas.forEach(tarea -> {
            tarea.setOrdenTrabajo(ordenFinal);
            tarea.setEstadoAnalisis(MuestraAnalisis.EstadoAnalisis.EN_PROCESO);
            muestraAnalisisRepository.save(tarea);
        });

        logger.info("Orden de trabajo creada con ID: {} para técnico: {} {} con {} tareas",
                ordenTrabajo.getId(),
                tecnico.getNombre(),
                tecnico.getApellido(),
                tareas.size());

        // Convertir y retornar DTO
        return convertirADto(ordenTrabajo, tareas);
    }

    /**
     * Obtiene una orden de trabajo por su ID
     */
    @Transactional(readOnly = true)
    public OrdenTrabajoDTO obtenerOrdenTrabajo(UUID id) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + id));

        List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ordenTrabajo);

        return convertirADto(ordenTrabajo, tareas);
    }

    /**
     * Lista todas las órdenes de trabajo
     */
    @Transactional(readOnly = true)
    public List<OrdenTrabajoDTO> listarOrdenesDetrabajo() {
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.findAllByOrderByFechaCreacionDesc();

        return ordenes.stream()
                .map(ot -> {
                    List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ot);
                    return convertirADto(ot, tareas);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lista órdenes de trabajo por técnico
     */
    @Transactional(readOnly = true)
    public List<OrdenTrabajoDTO> listarOrdenesPorTecnico(UUID tecnicoId) {
        List<OrdenTrabajo> ordenes = ordenTrabajoRepository.findByTecnicoAsignadoId(tecnicoId);

        return ordenes.stream()
                .map(ot -> {
                    List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ot);
                    return convertirADto(ot, tareas);
                })
                .collect(Collectors.toList());
    }

    /**
     * Actualiza el estado de una orden de trabajo
     */
    public OrdenTrabajoDTO actualizarEstado(UUID id, EstadoOT nuevoEstado) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + id));

        ordenTrabajo.setEstado(nuevoEstado);

        if (nuevoEstado == EstadoOT.FINALIZADA || nuevoEstado == EstadoOT.CANCELADA) {
            ordenTrabajo.setFechaFinalizacion(java.time.LocalDateTime.now());
            
            // Si se cancela, liberar tareas EN_PROCESO sin resultado
            if (nuevoEstado == EstadoOT.CANCELADA) {
                int tareasLiberadas = muestraAnalisisRepository.liberarTareasDeOrdenCancelada(id);
                logger.info("Se liberaron {} tareas de la orden cancelada {}", tareasLiberadas, id);
            }
        }

        ordenTrabajo = ordenTrabajoRepository.save(ordenTrabajo);

        logger.info("Estado de orden de trabajo {} actualizado a: {}", id, nuevoEstado);

        List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ordenTrabajo);
        return convertirADto(ordenTrabajo, tareas);
    }

    /**
     * Convierte OrdenTrabajo a DTO
     */
    private OrdenTrabajoDTO convertirADto(OrdenTrabajo ordenTrabajo, List<MuestraAnalisis> tareas) {
        OrdenTrabajoDTO dto = new OrdenTrabajoDTO();
        dto.setIdOrdenTrabajo(ordenTrabajo.getId());
        dto.setCodigoOT(ordenTrabajo.getCodigoOT());
        dto.setEstado(ordenTrabajo.getEstado().name());
        dto.setFechaCreacion(ordenTrabajo.getFechaCreacion());
        dto.setFechaFinalizacion(ordenTrabajo.getFechaFinalizacion());

        // Convertir técnico asignado
        Usuario tecnico = ordenTrabajo.getTecnicoAsignado();
        UsuarioBasicDTO tecnicoDTO = new UsuarioBasicDTO(
                tecnico.getId(),
                tecnico.getNombre(),
                tecnico.getApellido(),
                tecnico.getEmail()
        );
        dto.setTecnicoAsignado(tecnicoDTO);

        // Convertir tareas
        List<TareaBasicDTO> tareasDTO = tareas.stream()
                .<TareaBasicDTO>map(this::convertirTareaBasicDto)
                .collect(Collectors.toList());
        dto.setTareas(tareasDTO);

        // Estadísticas
        dto.setTotalTareas(tareas.size());
        dto.setTareasPendientes((int) tareas.stream()
                .filter(t -> t.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE)
                .count());
        dto.setTareasCompletadas((int) tareas.stream()
                .filter(t -> t.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.COMPLETADO)
                .count());

        return dto;
    }

    /**
     * Elimina una orden de trabajo y libera sus tareas asociadas
     */
    public void eliminarOrdenTrabajo(UUID id) {
        logger.info("Eliminando orden de trabajo: {}", id);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + id));

        // Solo se pueden eliminar órdenes en estado ABIERTA o CANCELADA
        if (ordenTrabajo.getEstado() == EstadoOT.FINALIZADA) {
            throw new ValidationException(
                    "No se puede eliminar una orden de trabajo finalizada. " +
                    "Solo se pueden eliminar órdenes abiertas o canceladas.");
        }

        // Obtener las tareas asociadas y liberarlas
        List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ordenTrabajo);
        
        for (MuestraAnalisis tarea : tareas) {
            tarea.setOrdenTrabajo(null);
            // Devolver las tareas al estado PENDIENTE
            tarea.setEstadoAnalisis(MuestraAnalisis.EstadoAnalisis.PENDIENTE);
            muestraAnalisisRepository.save(tarea);
        }

        // Eliminar la orden de trabajo
        ordenTrabajoRepository.delete(ordenTrabajo);

        logger.info("Orden de trabajo {} eliminada. Se liberaron {} tareas", 
                ordenTrabajo.getCodigoOT(), tareas.size());
    }

    /**
     * Convierte MuestraAnalisis a TareaBasicDTO
     */
    private TareaBasicDTO convertirTareaBasicDto(MuestraAnalisis ma) {
        TareaBasicDTO dto = new TareaBasicDTO();
        dto.setIdMuestraAnalisis(ma.getIdMuestraAnalisis());
        dto.setNumeroMuestra(ma.getMuestra().getNumeroInterno());
        dto.setNombreAnalisis(ma.getAnalisis().getNombreAnalisis());
        dto.setEstadoAnalisis(ma.getEstadoAnalisis().name());
        dto.setCumpleNormativa(ma.getCumpleNormativa());
        return dto;
    }
}
