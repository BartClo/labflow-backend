package com.labflow.controller;

import com.labflow.dto.InsumoCreateDTO;
import com.labflow.dto.InsumoDTO;
import com.labflow.service.InsumoService;
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
 * Controlador REST para gestión de insumos de laboratorio
 */
@RestController
@RequestMapping("/api/insumos")
@Tag(name = "Insumos", description = "API para gestión de insumos de laboratorio")
public class InsumoController {

    private final InsumoService insumoService;

    public InsumoController(InsumoService insumoService) {
        this.insumoService = insumoService;
    }

    @Operation(summary = "Obtener todos los insumos", 
               description = "Retorna la lista completa de insumos de laboratorio")
    @GetMapping
    public ResponseEntity<List<InsumoDTO>> obtenerTodos() {
        List<InsumoDTO> insumos = insumoService.obtenerTodos();
        return ResponseEntity.ok(insumos);
    }

    @Operation(summary = "Obtener insumos activos")
    @GetMapping("/activos")
    public ResponseEntity<List<InsumoDTO>> obtenerActivos() {
        List<InsumoDTO> insumos = insumoService.obtenerActivos();
        return ResponseEntity.ok(insumos);
    }

    @Operation(summary = "Obtener insumo por ID")
    @GetMapping("/{id}")
    public ResponseEntity<InsumoDTO> obtenerPorId(@PathVariable UUID id) {
        Optional<InsumoDTO> insumo = insumoService.obtenerPorId(id);
        return insumo.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo insumo")
    @PostMapping
    public ResponseEntity<InsumoDTO> crear(@Valid @RequestBody InsumoCreateDTO createDTO) {
        InsumoDTO nuevoInsumo = insumoService.crear(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInsumo);
    }

    @Operation(summary = "Actualizar insumo existente")
    @PutMapping("/{id}")
    public ResponseEntity<InsumoDTO> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody InsumoCreateDTO createDTO) {
        try {
            InsumoDTO insumoActualizado = insumoService.actualizar(id, createDTO);
            return ResponseEntity.ok(insumoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar insumo")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        try {
            insumoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
