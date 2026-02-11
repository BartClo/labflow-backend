package com.labflow.controller;

import com.labflow.dto.TareaPendienteDTO;
import com.labflow.dto.request.GuardarResultadoSimpleDTO;
import com.labflow.dto.request.ResultadoUpdateDTO;
import com.labflow.service.TareaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST para gestionar Tareas (MuestraAnalisis)
 */
@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    private static final Logger logger = LoggerFactory.getLogger(TareaController.class);

    @Autowired
    private TareaService tareaService;

    /**
     * Obtiene la "bolsa de pendientes" - tareas sin orden de trabajo asignada
     * GET /api/tareas/pendientes?analisisId={uuid}
     */
    @GetMapping("/pendientes")
    public ResponseEntity<List<TareaPendienteDTO>> obtenerTareasPendientes(
            @RequestParam(required = false) UUID analisisId) {

        logger.info("Solicitud de tareas pendientes. AnalisisId: {}", analisisId);

        List<TareaPendienteDTO> tareas = tareaService.obtenerTareasPendientes(analisisId);

        logger.info("Se encontraron {} tareas pendientes", tareas.size());

        return ResponseEntity.ok(tareas);
    }

    /**
     * Actualiza el resultado de una tarea y aplica validación NCh 409
     * PUT /api/tareas/{id}/resultado
     */
    @PutMapping("/{id}/resultado")
    public ResponseEntity<Map<String, Object>> actualizarResultado(
            @PathVariable UUID id,
            @Valid @RequestBody ResultadoUpdateDTO dto) {

        logger.info("Actualizando resultado de tarea: {}", id);

        Map<String, Object> response = tareaService.actualizarResultado(id, dto);

        return ResponseEntity.ok(response);
    }

    /**
     * Busca una tarea por código de barras QR en estado EN_PROCESO
     * GET /api/tareas/search?codigo={codigo_qr}
     */
    @GetMapping("/search")
    public ResponseEntity<TareaPendienteDTO> buscarPorCodigoBarras(
            @RequestParam String codigo) {

        logger.info("Búsqueda de tarea por código QR: {}", codigo);

        TareaPendienteDTO tarea = tareaService.buscarPorCodigoBarras(codigo);

        return ResponseEntity.ok(tarea);
    }

    /**
     * Guarda un resultado capturado de forma simplificada (un solo valor por muestra)
     * POST /api/tareas/guardar-resultado
     */
    @PostMapping("/guardar-resultado")
    public ResponseEntity<Map<String, Object>> guardarResultadoSimple(
            @Valid @RequestBody GuardarResultadoSimpleDTO dto) {

        logger.info("Guardando resultado simple para tarea: {}", dto.getIdMuestraAnalisis());

        Map<String, Object> response = tareaService.guardarResultadoSimple(dto);

        return ResponseEntity.ok(response);
    }

    /**
     * Agrupa tareas pendientes sin OT por tipo de análisis
     * GET /api/tareas/pendientes-por-analisis
     */
    @GetMapping("/pendientes-por-analisis")
    public ResponseEntity<Map<String, Long>> contarTareasPendientesPorAnalisis() {

        logger.info("Solicitud de conteo de tareas pendientes agrupadas por análisis");

        Map<String, Long> conteo = tareaService.contarTareasPendientesPorAnalisis();

        logger.info("Se encontraron {} tipos de análisis con tareas pendientes", conteo.size());

        return ResponseEntity.ok(conteo);
    }

    /**
     * Valida una tarea, cambiando su estado de COMPLETADO a VALIDADO
     * PATCH /api/tareas/{id}/validar
     */
    @PatchMapping("/{id}/validar")
    public ResponseEntity<Map<String, String>> validarTarea(@PathVariable UUID id) {

        logger.info("Validando tarea: {}", id);

        tareaService.validarTarea(id);

        Map<String, String> response = Map.of(
                "mensaje", "Tarea validada exitosamente",
                "tarea_id", id.toString()
        );

        return ResponseEntity.ok(response);
    }
}
