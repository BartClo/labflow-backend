package com.labflow.controller;

import com.labflow.dto.PlantillaCreateDTO;
import com.labflow.dto.PlantillaDTO;
import com.labflow.service.PlantillaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para gestión de plantillas de procedimientos
 */
@RestController
@RequestMapping("/api/plantillas")
@Tag(name = "Plantillas", description = "API para gestión de plantillas de procedimientos de laboratorio")
public class PlantillaController {

    private static final String ERROR_PREFIX = "Error: ";
    private final PlantillaService plantillaService;

    public PlantillaController(PlantillaService plantillaService) {
        this.plantillaService = plantillaService;
    }

    @Operation(summary = "Obtener todas las plantillas")
    @GetMapping
    public ResponseEntity<List<PlantillaDTO>> obtenerTodas() {
        List<PlantillaDTO> plantillas = plantillaService.obtenerTodas();
        return ResponseEntity.ok(plantillas);
    }

    @Operation(summary = "Obtener plantillas activas")
    @GetMapping("/activas")
    public ResponseEntity<List<PlantillaDTO>> obtenerActivas() {
        List<PlantillaDTO> plantillas = plantillaService.obtenerActivas();
        return ResponseEntity.ok(plantillas);
    }

    @Operation(summary = "Obtener plantilla por ID")
    @GetMapping("/{id}")
    public ResponseEntity<PlantillaDTO> obtenerPorId(@PathVariable UUID id) {
        Optional<PlantillaDTO> plantilla = plantillaService.obtenerPorId(id);
        return plantilla.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar plantillas por nombre")
    @GetMapping("/buscar")
    public ResponseEntity<List<PlantillaDTO>> buscarPorNombre(@RequestParam String nombre) {
        List<PlantillaDTO> plantillas = plantillaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(plantillas);
    }

    @Operation(summary = "Buscar plantillas por tipo de muestra")
    @GetMapping("/tipo-muestra/{tipoMuestra}")
    public ResponseEntity<List<PlantillaDTO>> buscarPorTipoMuestra(@PathVariable String tipoMuestra) {
        List<PlantillaDTO> plantillas = plantillaService.buscarPorTipoMuestra(tipoMuestra);
        return ResponseEntity.ok(plantillas);
    }

    @Operation(summary = "Crear nueva plantilla")
    @PostMapping
    public ResponseEntity<Object> crear(@RequestBody PlantillaCreateDTO createDTO) {
        try {
            PlantillaDTO nuevaPlantilla = plantillaService.crear(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPlantilla);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_PREFIX + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar plantilla")
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(@PathVariable UUID id, @RequestBody PlantillaCreateDTO updateDTO) {
        try {
            Optional<PlantillaDTO> plantillaActualizada = plantillaService.actualizar(id, updateDTO);
            return plantillaActualizada.map(plantilla -> ResponseEntity.ok((Object) plantilla))
                                     .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_PREFIX + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar plantilla")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        boolean eliminado = plantillaService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() 
                        : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Cambiar estado de plantilla")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PlantillaDTO> cambiarEstado(@PathVariable UUID id, @RequestParam String estado) {
        Optional<PlantillaDTO> plantillaActualizada = plantillaService.cambiarEstado(id, estado);
        return plantillaActualizada.map(ResponseEntity::ok)
                                  .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Agregar análisis a plantilla")
    @PostMapping("/{idPlantilla}/analisis/{idAnalisis}")
    public ResponseEntity<Object> agregarAnalisis(@PathVariable UUID idPlantilla, 
                                                  @PathVariable UUID idAnalisis,
                                                  @RequestParam(required = false) Integer orden) {
        try {
            Optional<PlantillaDTO> plantillaActualizada = plantillaService.agregarAnalisis(idPlantilla, idAnalisis, orden);
            return plantillaActualizada.map(plantilla -> ResponseEntity.ok((Object) plantilla))
                                     .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(ERROR_PREFIX + e.getMessage());
        }
    }

    @Operation(summary = "Remover análisis de plantilla")
    @DeleteMapping("/{idPlantilla}/analisis/{idAnalisis}")
    public ResponseEntity<PlantillaDTO> removerAnalisis(@PathVariable UUID idPlantilla, @PathVariable UUID idAnalisis) {
        Optional<PlantillaDTO> plantillaActualizada = plantillaService.removerAnalisis(idPlantilla, idAnalisis);
        return plantillaActualizada.map(ResponseEntity::ok)
                                  .orElse(ResponseEntity.notFound().build());
    }
}