package com.labflow.service;

import com.labflow.dto.OrdenTrabajoDTO;
import com.labflow.dto.OrdenTrabajoEtapaDTO;
import com.labflow.dto.WorkflowProgressDTO;
import com.labflow.dto.request.OrdenTrabajoCreateDTO;
import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.*;
import com.labflow.repository.MuestraAnalisisRepository;
import com.labflow.repository.OrdenTrabajoEtapaRepository;
import com.labflow.repository.OrdenTrabajoRepository;
import com.labflow.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing workflow stages in Órdenes de Trabajo.
 * Handles stage progression, completion tracking, and rejected sample handling.
 */
@Service
@Transactional
public class WorkflowService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowService.class);

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private OrdenTrabajoEtapaRepository ordenTrabajoEtapaRepository;

    @Autowired
    private MuestraAnalisisRepository muestraAnalisisRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    @Lazy
    private OrdenTrabajoService ordenTrabajoService;

    /**
     * Initializes the workflow for an Orden de Trabajo by creating all 4 stages.
     * Stages are created in PENDIENTE state, with the first stage automatically started.
     * 
     * @param ordenTrabajo The work order to initialize workflow for
     */
    public void inicializarWorkflow(OrdenTrabajo ordenTrabajo) {
        logger.info("Inicializando workflow para OT: {}", ordenTrabajo.getCodigoOT());

        // Create all 4 workflow stages
        List<OrdenTrabajoEtapa> etapas = new ArrayList<>();
        Usuario tecnico = ordenTrabajo.getTecnicoAsignado();

        for (TipoEtapaWorkflow tipo : TipoEtapaWorkflow.values()) {
            OrdenTrabajoEtapa etapa = new OrdenTrabajoEtapa(ordenTrabajo, tipo, tecnico);
            etapas.add(etapa);
        }

        // Save all stages
        ordenTrabajoEtapaRepository.saveAll(etapas);

        // Auto-start the first stage (Registro de Recepción)
        etapas.stream()
                .filter(e -> e.getTipoEtapa() == TipoEtapaWorkflow.REGISTRO_RECEPCION)
                .findFirst()
                .ifPresent(etapa -> {
                    etapa.iniciar();
                    ordenTrabajoEtapaRepository.save(etapa);
                    logger.info("Etapa inicial '{}' iniciada automáticamente para OT: {}", 
                            etapa.getTipoEtapa().getDisplayName(), ordenTrabajo.getCodigoOT());
                });
    }

    /**
     * Advances the workflow by completing the current stage and starting the next one.
     * Validates that stages are completed in sequential order.
     * 
     * @param ordenTrabajoId The work order ID
     * @param notas Optional notes about the stage completion
     * @return Updated workflow progress
     * @throws ResourceNotFoundException if OT not found
     * @throws ValidationException if stage cannot be advanced
     */
    public WorkflowProgressDTO avanzarEtapa(UUID ordenTrabajoId, String notas) {
        logger.info("Avanzando etapa para OT ID: {}", ordenTrabajoId);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de Trabajo no encontrada con ID: " + ordenTrabajoId));

        // Get current active stage
        OrdenTrabajoEtapa etapaActual = ordenTrabajo.obtenerEtapaActual();
        
        if (etapaActual == null) {
            throw new ValidationException("No hay etapas pendientes. El workflow ya está completo.");
        }

        if (!etapaActual.estaActiva()) {
            throw new ValidationException(
                    String.format("La etapa '%s' no está activa. Estado actual: %s", 
                            etapaActual.getTipoEtapa().getDisplayName(), 
                            etapaActual.getEstadoEtapa().getDisplayName())
            );
        }

        // Complete current stage
        etapaActual.completar(notas);
        ordenTrabajoEtapaRepository.save(etapaActual);
        logger.info("Etapa '{}' completada para OT: {}", 
                etapaActual.getTipoEtapa().getDisplayName(), ordenTrabajo.getCodigoOT());

        // Start next stage if exists
        TipoEtapaWorkflow siguienteTipo = etapaActual.getTipoEtapa().getSiguienteEtapa();
        if (siguienteTipo != null) {
            OrdenTrabajoEtapa siguienteEtapa = ordenTrabajoEtapaRepository
                    .findByOrdenTrabajoAndTipoEtapa(ordenTrabajo, siguienteTipo)
                    .orElseThrow(() -> new ValidationException("No se encontró la siguiente etapa del workflow"));

            siguienteEtapa.iniciar();
            ordenTrabajoEtapaRepository.save(siguienteEtapa);
            logger.info("Siguiente etapa '{}' iniciada para OT: {}", 
                    siguienteEtapa.getTipoEtapa().getDisplayName(), ordenTrabajo.getCodigoOT());
        } else {
            // Last stage completed - auto-finalize OT
            logger.info("Última etapa completada. Finalizando OT: {}", ordenTrabajo.getCodigoOT());
            if (ordenTrabajo.estaWorkflowCompleto()) {
                ordenTrabajo.completar();
                ordenTrabajoRepository.save(ordenTrabajo);
                logger.info("OT {} finalizada automáticamente", ordenTrabajo.getCodigoOT());
            }
        }

        return obtenerProgreso(ordenTrabajoId);
    }

    /**
     * Gets the current workflow progress for an Orden de Trabajo.
     * 
     * @param ordenTrabajoId The work order ID
     * @return Workflow progress with all stages
     * @throws ResourceNotFoundException if OT not found
     */
    public WorkflowProgressDTO obtenerProgreso(UUID ordenTrabajoId) {
        logger.debug("Obteniendo progreso de workflow para OT ID: {}", ordenTrabajoId);

        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de Trabajo no encontrada con ID: " + ordenTrabajoId));

        List<OrdenTrabajoEtapa> etapas = ordenTrabajoEtapaRepository
                .findByOrdenTrabajoOrderByOrdenSecuenciaAsc(ordenTrabajo);

        List<OrdenTrabajoEtapaDTO> etapasDTO = etapas.stream()
                .map(this::convertirEtapaADto)
                .collect(Collectors.toList());

        return new WorkflowProgressDTO(etapasDTO);
    }

    /**
     * Creates a new Orden de Trabajo with the rejected samples from an existing OT.
     * Allows manual reprocessing of samples that failed normative validation.
     * 
     * @param ordenTrabajoOriginalId The original OT ID with rejected samples
     * @param tecnicoAsignadoId Optional - new technician ID (if null, uses original OT's technician)
     * @param notas Optional notes for the new OT
     * @return DTO of the newly created OT
     * @throws ResourceNotFoundException if OT not found
     * @throws ValidationException if no rejected samples exist
     */
    public OrdenTrabajoDTO crearOTConMuestrasRechazadas(UUID ordenTrabajoOriginalId, UUID tecnicoAsignadoId, String notas, List<UUID> tareaIdsARechazar) {
        logger.info("Creando nueva OT con muestras rechazadas de OT ID: {}", ordenTrabajoOriginalId);

        OrdenTrabajo otOriginal = ordenTrabajoRepository.findById(ordenTrabajoOriginalId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden de Trabajo no encontrada con ID: " + ordenTrabajoOriginalId));

        // If specific task IDs were provided, mark them as rejected (cumpleNormativa = false)
        if (tareaIdsARechazar != null && !tareaIdsARechazar.isEmpty()) {
            for (MuestraAnalisis tarea : otOriginal.getTareas()) {
                if (tareaIdsARechazar.contains(tarea.getIdMuestraAnalisis())) {
                    tarea.setCumpleNormativa(false);
                    muestraAnalisisRepository.save(tarea);
                    logger.info("Tarea {} marcada como rechazada (cumpleNormativa=false)", tarea.getIdMuestraAnalisis());
                }
            }
        }

        // Get rejected samples
        List<MuestraAnalisis> muestrasRechazadas = otOriginal.obtenerMuestrasRechazadas();
        
        if (muestrasRechazadas.isEmpty()) {
            throw new ValidationException(
                    String.format("La OT %s no tiene muestras rechazadas", otOriginal.getCodigoOT())
            );
        }

        logger.info("Se encontraron {} muestras rechazadas en OT: {}", 
                muestrasRechazadas.size(), otOriginal.getCodigoOT());

        // Determine technician (use provided or inherit from original)
        Usuario tecnico;
        if (tecnicoAsignadoId != null) {
            tecnico = usuarioRepository.findById(tecnicoAsignadoId)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con ID: " + tecnicoAsignadoId));
        } else {
            tecnico = otOriginal.getTecnicoAsignado();
        }

        // Reset rejected samples to PENDIENTE so they can be reassigned
        List<UUID> tareaIds = muestrasRechazadas.stream()
                .map(MuestraAnalisis::getIdMuestraAnalisis)
                .collect(Collectors.toList());

        // Remove from original OT
        for (MuestraAnalisis tarea : muestrasRechazadas) {
            tarea.setOrdenTrabajo(null);
            tarea.setEstadoAnalisis(MuestraAnalisis.EstadoAnalisis.PENDIENTE);
            tarea.setFechaInicio(null);
            tarea.setFechaFinalizacion(null);
            tarea.setCumpleNormativa(null);
            tarea.setCumpleNorma(null);
            muestraAnalisisRepository.save(tarea);
        }

        // Create new OT with rejected samples
        OrdenTrabajoCreateDTO createDTO = new OrdenTrabajoCreateDTO();
        createDTO.setTecnicoAsignadoId(tecnico.getId());
        createDTO.setTareaIds(tareaIds);

        OrdenTrabajoDTO nuevaOT = ordenTrabajoService.crearOrdenTrabajo(createDTO);
        
        logger.info("Nueva OT {} creada con {} muestras rechazadas de OT {}", 
                nuevaOT.getCodigoOT(), tareaIds.size(), otOriginal.getCodigoOT());

        return nuevaOT;
    }

    /**
     * Converts an OrdenTrabajoEtapa entity to DTO.
     */
    private OrdenTrabajoEtapaDTO convertirEtapaADto(OrdenTrabajoEtapa etapa) {
        OrdenTrabajoEtapaDTO dto = new OrdenTrabajoEtapaDTO();
        dto.setIdEtapa(etapa.getId());
        dto.setTipoEtapa(etapa.getTipoEtapa().name());
        dto.setNombreEtapa(etapa.getTipoEtapa().getDisplayName());
        dto.setOrdenSecuencia(etapa.getOrdenSecuencia());
        dto.setEstadoEtapa(etapa.getEstadoEtapa().name());
        dto.setEstadoEtapaDisplay(etapa.getEstadoEtapa().getDisplayName());
        dto.setFechaInicio(etapa.getFechaInicio());
        dto.setFechaCompletado(etapa.getFechaCompletado());
        dto.setNotas(etapa.getNotas());
        dto.setCreatedAt(etapa.getCreatedAt());
        dto.setUpdatedAt(etapa.getUpdatedAt());

        if (etapa.getTecnicoAsignado() != null) {
            dto.setTecnicoAsignadoId(etapa.getTecnicoAsignado().getId());
            dto.setTecnicoAsignado(etapa.getTecnicoAsignado().getNombre());
        }

        return dto;
    }
}
