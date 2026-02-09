package com.labflow.controller;

import com.labflow.dto.CompletarEtapaDTO;
import com.labflow.dto.CrearOTRechazadasDTO;
import com.labflow.dto.OrdenTrabajoDTO;
import com.labflow.dto.WorkflowProgressDTO;
import com.labflow.dto.request.OrdenTrabajoCreateDTO;
import com.labflow.model.EstadoOT;
import com.labflow.service.OrdenTrabajoService;
import com.labflow.service.WorkflowService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gestionar Órdenes de Trabajo
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenTrabajoController {

    private static final Logger logger = LoggerFactory.getLogger(OrdenTrabajoController.class);

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @Autowired
    private WorkflowService workflowService;

    /**
     * Crea una nueva orden de trabajo agrupando tareas
     * POST /api/ordenes
     */
    @PostMapping
    public ResponseEntity<OrdenTrabajoDTO> crearOrdenTrabajo(
            @Valid @RequestBody OrdenTrabajoCreateDTO dto) {

        logger.info("Solicitud para crear orden de trabajo con {} tareas", dto.getTareaIds().size());

        OrdenTrabajoDTO ordenCreada = ordenTrabajoService.crearOrdenTrabajo(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ordenCreada);
    }

    /**
     * Obtiene una orden de trabajo por su ID
     * GET /api/ordenes/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrdenTrabajoDTO> obtenerOrdenTrabajo(@PathVariable UUID id) {
        logger.info("Solicitud de orden de trabajo: {}", id);

        OrdenTrabajoDTO orden = ordenTrabajoService.obtenerOrdenTrabajo(id);

        return ResponseEntity.ok(orden);
    }

    /**
     * Lista todas las órdenes de trabajo
     * GET /api/ordenes
     */
    @GetMapping
    public ResponseEntity<List<OrdenTrabajoDTO>> listarOrdenesDeTrabajo() {
        logger.info("Solicitud de lista de órdenes de trabajo");

        List<OrdenTrabajoDTO> ordenes = ordenTrabajoService.listarOrdenesDetrabajo();

        return ResponseEntity.ok(ordenes);
    }

    /**
     * Lista órdenes de trabajo por técnico
     * GET /api/ordenes/tecnico/{tecnicoId}
     */
    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<OrdenTrabajoDTO>> listarOrdenesPorTecnico(
            @PathVariable UUID tecnicoId) {

        logger.info("Solicitud de órdenes para técnico: {}", tecnicoId);

        List<OrdenTrabajoDTO> ordenes = ordenTrabajoService.listarOrdenesPorTecnico(tecnicoId);

        return ResponseEntity.ok(ordenes);
    }

    /**
     * Actualiza el estado de una orden de trabajo
     * PUT /api/ordenes/{id}/estado
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<OrdenTrabajoDTO> actualizarEstado(
            @PathVariable UUID id,
            @RequestParam EstadoOT estado) {

        logger.info("Actualizando estado de orden {} a: {}", id, estado);

        OrdenTrabajoDTO ordenActualizada = ordenTrabajoService.actualizarEstado(id, estado);

        return ResponseEntity.ok(ordenActualizada);
    }

    /**
     * Obtiene el progreso del workflow de una orden de trabajo
     * GET /api/ordenes/{id}/workflow
     */
    @GetMapping("/{id}/workflow")
    public ResponseEntity<WorkflowProgressDTO> obtenerWorkflow(@PathVariable UUID id) {
        logger.info("Solicitud de workflow para orden: {}", id);

        WorkflowProgressDTO progreso = workflowService.obtenerProgreso(id);

        return ResponseEntity.ok(progreso);
    }

    /**
     * Completa la etapa actual del workflow y avanza a la siguiente
     * POST /api/ordenes/{id}/workflow/completar-etapa
     */
    @PostMapping("/{id}/workflow/completar-etapa")
    public ResponseEntity<WorkflowProgressDTO> completarEtapa(
            @PathVariable UUID id,
            @RequestBody(required = false) CompletarEtapaDTO dto) {

        logger.info("Completando etapa actual de orden: {}", id);

        String notas = (dto != null) ? dto.getNotas() : null;
        WorkflowProgressDTO progreso = workflowService.avanzarEtapa(id, notas);

        return ResponseEntity.ok(progreso);
    }

    /**
     * Crea una nueva orden de trabajo con las muestras rechazadas de esta orden
     * POST /api/ordenes/{id}/crear-ot-rechazadas
     */
    @PostMapping("/{id}/crear-ot-rechazadas")
    public ResponseEntity<OrdenTrabajoDTO> crearOTConMuestrasRechazadas(
            @PathVariable UUID id,
            @RequestBody(required = false) CrearOTRechazadasDTO dto) {

        logger.info("Creando nueva OT con muestras rechazadas de orden: {}", id);

        UUID tecnicoId = (dto != null) ? dto.getTecnicoAsignadoId() : null;
        String notas = (dto != null) ? dto.getNotas() : null;

        OrdenTrabajoDTO nuevaOT = workflowService.crearOTConMuestrasRechazadas(id, tecnicoId, notas);

        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOT);
    }

    /**
     * Elimina una orden de trabajo
     * DELETE /api/ordenes/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOrdenTrabajo(@PathVariable UUID id) {
        logger.info("Solicitud para eliminar orden de trabajo: {}", id);

        ordenTrabajoService.eliminarOrdenTrabajo(id);

        return ResponseEntity.noContent().build();
    }
}
