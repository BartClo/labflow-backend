package com.labflow.controller;

import com.labflow.dto.ReactivoCreateDTO;
import com.labflow.dto.ReactivoDTO;
import com.labflow.service.ReactivoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para gestión de reactivos químicos
 */
@RestController
@RequestMapping("/api/reactivos")
@Tag(name = "Reactivos", description = "API para gestión de reactivos químicos de laboratorio")
public class ReactivoController {

    private final ReactivoService reactivoService;

    public ReactivoController(ReactivoService reactivoService) {
        this.reactivoService = reactivoService;
    }

    @Operation(summary = "Obtener todos los reactivos", 
               description = "Retorna la lista completa de reactivos químicos")
    @GetMapping
    public ResponseEntity<List<ReactivoDTO>> obtenerTodos() {
        List<ReactivoDTO> reactivos = reactivoService.obtenerTodos();
        return ResponseEntity.ok(reactivos);
    }

    @Operation(summary = "Obtener reactivos activos")
    @GetMapping("/activos")
    public ResponseEntity<List<ReactivoDTO>> obtenerActivos() {
        List<ReactivoDTO> reactivos = reactivoService.obtenerActivos();
        return ResponseEntity.ok(reactivos);
    }

    @Operation(summary = "Obtener reactivo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ReactivoDTO> obtenerPorId(@PathVariable UUID id) {
        Optional<ReactivoDTO> reactivo = reactivoService.obtenerPorId(id);
        return reactivo.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reactivo por código")
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ReactivoDTO> obtenerPorCodigo(@PathVariable String codigo) {
        Optional<ReactivoDTO> reactivo = reactivoService.obtenerPorCodigo(codigo);
        return reactivo.map(ResponseEntity::ok)
                       .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener reactivos próximos a vencer",
               description = "Retorna reactivos que vencen antes de la fecha especificada")
    @GetMapping("/vencimiento/antes")
    public ResponseEntity<List<ReactivoDTO>> obtenerPorVencimientoCercano(
            @Parameter(description = "Fecha límite de vencimiento (formato: yyyy-MM-dd)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        List<ReactivoDTO> reactivos = reactivoService.obtenerPorVencimientoCercano(fecha);
        return ResponseEntity.ok(reactivos);
    }

    @Operation(summary = "Crear nuevo reactivo")
    @PostMapping
    public ResponseEntity<ReactivoDTO> crear(@Valid @RequestBody ReactivoCreateDTO createDTO) {
        ReactivoDTO nuevoReactivo = reactivoService.crear(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoReactivo);
    }

    @Operation(summary = "Actualizar reactivo existente")
    @PutMapping("/{id}")
    public ResponseEntity<ReactivoDTO> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ReactivoCreateDTO createDTO) {
        try {
            ReactivoDTO reactivoActualizado = reactivoService.actualizar(id, createDTO);
            return ResponseEntity.ok(reactivoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar reactivo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            reactivoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
