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
import com.labflow.repository.ParametroRepository;
import com.labflow.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    @Autowired
    private ParametroRepository parametroRepository;

    @Autowired
    @Lazy
    private WorkflowService workflowService;

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
        ordenTrabajo.setPrioridad(parsePrioridadConDefault(dto.getPrioridad()));

        // Guardar la orden de trabajo
        ordenTrabajo = ordenTrabajoRepository.save(ordenTrabajo);

        // Inicializar workflow con las 4 etapas estándar
        workflowService.inicializarWorkflow(ordenTrabajo);

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
    public List<OrdenTrabajoDTO> listarOrdenesDetrabajo(String prioridadRaw) {
        OrdenTrabajo.PrioridadOT prioridad = parsePrioridad(prioridadRaw);

        List<OrdenTrabajo> ordenes;
        if (prioridad != null) {
            ordenes = ordenTrabajoRepository.findByPrioridadOrderByFechaCreacionDesc(prioridad);
        } else {
            ordenes = ordenTrabajoRepository.findAllByOrderByFechaCreacionDesc();
        }

        return ordenes.stream()
                .map(ot -> {
                    List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ot);
                    return convertirADto(ot, tareas);
                })
                .collect(Collectors.toList());
    }

    public OrdenTrabajoDTO cambiarPrioridad(UUID id, String nuevaPrioridad) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + id));

        if (nuevaPrioridad == null || nuevaPrioridad.isBlank()) {
            throw new ValidationException("La prioridad es requerida");
        }
        OrdenTrabajo.PrioridadOT prioridad = parsePrioridad(nuevaPrioridad);

        ordenTrabajo.setPrioridad(prioridad);
        ordenTrabajo = ordenTrabajoRepository.save(ordenTrabajo);

        List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ordenTrabajo);
        return convertirADto(ordenTrabajo, tareas);
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

        // Validar que el workflow esté completo antes de finalizar
        if (nuevoEstado == EstadoOT.FINALIZADA && !ordenTrabajo.estaWorkflowCompleto()) {
            throw new ValidationException(
                    "No se puede finalizar la orden de trabajo. El workflow debe estar completo (todas las etapas finalizadas).");
        }

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
        dto.setPrioridad(ordenTrabajo.getPrioridad().name());
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

        // Información de muestras rechazadas
        dto.setCantidadMuestrasRechazadas((int) ordenTrabajo.contarMuestrasRechazadas());
        
        // Nota: El progreso del workflow se obtiene mediante el endpoint separado
        // GET /api/ordenes/{id}/workflow para evitar dependencia circular

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

        // Solo se pueden eliminar órdenes en estado ABIERTA, CANCELADA o EN_PROCESO
        // NO se permite eliminar órdenes FINALIZADAS
        if (ordenTrabajo.getEstado() == EstadoOT.FINALIZADA) {
            throw new ValidationException(
                    "No se puede eliminar una orden de trabajo finalizada. " +
                    "Una vez completada, la orden debe conservarse para auditoría. " +
                    "Solo se pueden eliminar órdenes abiertas, en proceso o canceladas.");
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
     * Incluye límites normativos del parámetro principal del análisis
     */
    private TareaBasicDTO convertirTareaBasicDto(MuestraAnalisis ma) {
        TareaBasicDTO dto = new TareaBasicDTO();
        dto.setIdMuestraAnalisis(ma.getIdMuestraAnalisis());
        dto.setNumeroMuestra(ma.getMuestra().getNumeroInterno());
        dto.setCodigoBarras(ma.getMuestra().getCodigoBarras());
        dto.setNombreAnalisis(ma.getAnalisis().getNombreAnalisis());
        dto.setEstadoAnalisis(ma.getEstadoAnalisis().name());
        dto.setCumpleNormativa(ma.getCumpleNormativa());

        if (ma.getResultado() != null) {
            try {
                // By default the fallback logic maps inside either root or the parameter key
                if (ma.getResultado().containsKey("valor_medido")) {
                    Object val = ma.getResultado().get("valor_medido");
                    if (val instanceof Number) {
                        dto.setValorMedido(((Number) val).doubleValue());
                    } else if (val instanceof String) {
                        dto.setValorMedido(Double.parseDouble((String) val));
                    }
                }
                if (ma.getResultado().containsKey("observaciones")) {
                    dto.setObservaciones((String) ma.getResultado().get("observaciones"));
                }
                
                // Si el formato es por parametro: {"parametros": {"Hierro": {"valor": 10 ...}}}
                if (ma.getResultado().containsKey("parametros")) {
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> parametros = (java.util.Map<String, Object>) ma.getResultado().get("parametros");
                    if (parametros != null && !parametros.isEmpty()) {
                        for (Object paramValObj : parametros.values()) {
                            if (paramValObj instanceof java.util.Map) {
                                @SuppressWarnings("unchecked")
                                java.util.Map<String, Object> paramVal = (java.util.Map<String, Object>) paramValObj;
                                // Chequear valor, o retrospectivo valor_medido
                                Object val = paramVal.get("valor");
                                if (val == null) {
                                    val = paramVal.get("valor_medido");
                                }
                                if (val != null && dto.getValorMedido() == null) {
                                    if (val instanceof Number) dto.setValorMedido(((Number) val).doubleValue());
                                    else if (val instanceof String) dto.setValorMedido(Double.parseDouble((String) val));
                                }
                                if (paramVal.containsKey("observaciones") && dto.getObservaciones() == null) {
                                    dto.setObservaciones((String) paramVal.get("observaciones"));
                                }
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                logger.warn("Error parsing resultado for ma {}: {}", ma.getIdMuestraAnalisis(), ex.getMessage());
            }
        }

        // Incluir prioridad de la muestra
        try {
            if (ma.getMuestra() != null && ma.getMuestra().getPrioridad() != null) {
                dto.setPrioridad(ma.getMuestra().getPrioridad().name());
            }
        } catch (Exception e) {
            logger.debug("No se pudo obtener prioridad para tarea {}", ma.getIdMuestraAnalisis());
        }

        // Enriquecer con límites del parámetro principal del análisis
        try {
            java.util.List<Parametro> parametros = parametroRepository.findByAnalisisId(
                    ma.getAnalisis().getIdAnalisis());
            if (!parametros.isEmpty()) {
                Parametro param = parametros.get(0);
                dto.setUnidadMedida(param.getUnidad());
                dto.setLimiteMinimo(param.getValorMinimoNormativa());
                dto.setLimiteMaximo(param.getValorMaximoNormativa());
                dto.setNombreParametro(param.getNombre());
                // Incluir el id de la muestra para permitir acciones sobre la muestra desde frontend
                try {
                    dto.setIdMuestra(ma.getMuestra().getIdMuestra());
                } catch (Exception ex) {
                    logger.debug("No se pudo obtener idMuestra para tarea {}: {}", ma.getIdMuestraAnalisis(), ex.getMessage());
                }
            }
        } catch (Exception e) {
            logger.warn("No se pudieron cargar parámetros para análisis {}: {}",
                    ma.getAnalisis().getIdAnalisis(), e.getMessage());
        }

        return dto;
    }

    /**
     * Obtiene estadísticas del sistema de órdenes de trabajo
     */
    public EstadisticasOTDTO obtenerEstadisticas() {
        logger.debug("Obteniendo estadísticas de órdenes de trabajo");

        List<OrdenTrabajo> todasLasOT = ordenTrabajoRepository.findAll();

        // OT Activas: ABIERTA o EN_PROCESO
        long otActivas = todasLasOT.stream()
                .filter(ot -> ot.getEstado() == EstadoOT.ABIERTA || ot.getEstado() == EstadoOT.EN_PROCESO)
                .count();

        // OT Urgentes: Calcular basándose en muestras con prioridad ALTA
        long otUrgentes = todasLasOT.stream()
                .filter(ot -> {
                    if (ot.getEstado() == EstadoOT.ABIERTA || ot.getEstado() == EstadoOT.EN_PROCESO) {
                        return ot.getTareas().stream()
                                .anyMatch(t -> t.getMuestra() != null 
                                        && t.getMuestra().getPrioridad() == Muestra.Prioridad.ALTA);
                    }
                    return false;
                })
                .count();

        // OT En Proceso: solo EN_PROCESO
        long otEnProceso = todasLasOT.stream()
                .filter(ot -> ot.getEstado() == EstadoOT.EN_PROCESO)
                .count();

        return new EstadisticasOTDTO(otActivas, otUrgentes, otEnProceso);
    }

    /**
     * DTO para estadísticas de órdenes de trabajo
     */
    public record EstadisticasOTDTO(
            long otActivas,
            long otUrgentes,
            long otEnProceso
    ) {}

    private OrdenTrabajo.PrioridadOT parsePrioridad(String prioridadRaw) {
        if (prioridadRaw == null || prioridadRaw.isBlank()) {
            return null;
        }

        try {
            return OrdenTrabajo.PrioridadOT.valueOf(prioridadRaw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new ValidationException("La prioridad debe ser ALTA, MEDIA o BAJA");
        }
    }

    private OrdenTrabajo.PrioridadOT parsePrioridadConDefault(String prioridadRaw) {
        OrdenTrabajo.PrioridadOT prioridad = parsePrioridad(prioridadRaw);
        return prioridad != null ? prioridad : OrdenTrabajo.PrioridadOT.MEDIA;
    }
}
