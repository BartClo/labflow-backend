package com.labflow.controller;

import com.labflow.dto.TareaPendienteDTO;
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
}
