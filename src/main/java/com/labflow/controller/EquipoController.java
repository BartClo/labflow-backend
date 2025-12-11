package com.labflow.controller;

import com.labflow.dto.EquipoCreateDTO;
import com.labflow.dto.EquipoDTO;
import com.labflow.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Controlador REST para gestión de equipos de laboratorio
 */
@RestController
@RequestMapping("/api/equipos")
@Tag(name = "Equipos", description = "API para gestión de equipos de laboratorio")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @Operation(summary = "Obtener todos los equipos", 
               description = "Retorna la lista completa de equipos de laboratorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de equipos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<EquipoDTO>> obtenerTodos() {
        List<EquipoDTO> equipos = equipoService.obtenerTodos();
        return ResponseEntity.ok(equipos);
    }

    @Operation(summary = "Obtener equipos activos", 
               description = "Retorna solo los equipos con estado activo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de equipos activos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class)))
    })
    @GetMapping("/activos")
    public ResponseEntity<List<EquipoDTO>> obtenerActivos() {
        List<EquipoDTO> equipos = equipoService.obtenerActivos();
        return ResponseEntity.ok(equipos);
    }

    @Operation(summary = "Obtener equipo por ID", 
               description = "Retorna un equipo específico basado en su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Equipo encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Equipo no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> obtenerPorId(
            @Parameter(description = "ID único del equipo", required = true)
            @PathVariable UUID id) {
        Optional<EquipoDTO> equipo = equipoService.obtenerPorId(id);
        return equipo.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener equipo por código", 
               description = "Retorna un equipo específico basado en su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Equipo encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Equipo no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<EquipoDTO> obtenerPorCodigo(
            @Parameter(description = "Código del equipo", required = true)
            @PathVariable String codigo) {
        Optional<EquipoDTO> equipo = equipoService.obtenerPorCodigo(codigo);
        return equipo.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar equipos por marca", 
               description = "Busca equipos que contengan el texto especificado en la marca")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class)))
    })
    @GetMapping("/buscar/marca")
    public ResponseEntity<List<EquipoDTO>> buscarPorMarca(
            @Parameter(description = "Texto a buscar en la marca", required = true)
            @RequestParam String marca) {
        List<EquipoDTO> equipos = equipoService.buscarPorMarca(marca);
        return ResponseEntity.ok(equipos);
    }

    @Operation(summary = "Crear nuevo equipo", 
               description = "Crea un nuevo equipo de laboratorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Equipo creado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<EquipoDTO> crear(
            @Parameter(description = "Datos del equipo a crear", required = true)
            @Valid @RequestBody EquipoCreateDTO createDTO) {
        EquipoDTO nuevoEquipo = equipoService.crear(createDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEquipo);
    }

    @Operation(summary = "Actualizar equipo existente", 
               description = "Actualiza los datos de un equipo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Equipo actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = EquipoDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Equipo no encontrado"),
        @ApiResponse(responseCode = "400", 
                    description = "Datos de entrada inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EquipoDTO> actualizar(
            @Parameter(description = "ID del equipo a actualizar", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del equipo", required = true)
            @Valid @RequestBody EquipoCreateDTO createDTO) {
        try {
            EquipoDTO equipoActualizado = equipoService.actualizar(id, createDTO);
            return ResponseEntity.ok(equipoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar equipo", 
               description = "Desactiva un equipo (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", 
                    description = "Equipo eliminado exitosamente"),
        @ApiResponse(responseCode = "404", 
                    description = "Equipo no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del equipo a eliminar", required = true)
            @PathVariable UUID id) {
        try {
            equipoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
