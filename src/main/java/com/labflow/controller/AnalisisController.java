package com.labflow.controller;

import com.labflow.dto.AnalisisCreateDTO;
import com.labflow.dto.AnalisisDTO;
import com.labflow.service.AnalisisService;
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
 * Controlador REST para gestión de análisis de laboratorio
 */
@RestController
@RequestMapping("/api/analisis")
@Tag(name = "Análisis", description = "API para gestión de análisis de laboratorio")
public class AnalisisController {

    private final AnalisisService analisisService;

    public AnalisisController(AnalisisService analisisService) {
        this.analisisService = analisisService;
    }

    @Operation(summary = "Obtener todos los análisis", 
               description = "Retorna la lista completa de análisis disponibles en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de análisis obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<AnalisisDTO>> obtenerTodos() {
        List<AnalisisDTO> analisis = analisisService.obtenerTodos();
        return ResponseEntity.ok(analisis);
    }

    @Operation(summary = "Obtener análisis activos", 
               description = "Retorna solo los análisis con estado 'Activo'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de análisis activos obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class)))
    })
    @GetMapping("/activos")
    public ResponseEntity<List<AnalisisDTO>> obtenerActivos() {
        List<AnalisisDTO> analisis = analisisService.obtenerActivos();
        return ResponseEntity.ok(analisis);
    }

    @Operation(summary = "Obtener análisis por ID", 
               description = "Retorna un análisis específico basado en su ID único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Análisis encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Análisis no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AnalisisDTO> obtenerPorId(
            @Parameter(description = "ID único del análisis", required = true)
            @PathVariable UUID id) {
        Optional<AnalisisDTO> analisis = analisisService.obtenerPorId(id);
        return analisis.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Obtener análisis por código", 
               description = "Retorna un análisis específico basado en su código único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Análisis encontrado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Análisis no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<AnalisisDTO> obtenerPorCodigo(
            @Parameter(description = "Código único del análisis", required = true)
            @PathVariable String codigo) {
        Optional<AnalisisDTO> analisis = analisisService.obtenerPorCodigo(codigo);
        return analisis.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar análisis por categoría", 
               description = "Retorna análisis que pertenecen a una categoría específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class)))
    })
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<AnalisisDTO>> buscarPorCategoria(
            @Parameter(description = "Categoría del análisis", required = true)
            @PathVariable String categoria) {
        List<AnalisisDTO> analisis = analisisService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(analisis);
    }

    @Operation(summary = "Buscar análisis por nombre", 
               description = "Retorna análisis cuyo nombre contenga el texto especificado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<AnalisisDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre del análisis", required = true)
            @RequestParam String nombre) {
        List<AnalisisDTO> analisis = analisisService.buscarPorNombre(nombre);
        return ResponseEntity.ok(analisis);
    }

    @Operation(summary = "Crear nuevo análisis", 
               description = "Crea un nuevo análisis en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Análisis creado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Datos inválidos o código duplicado"),
        @ApiResponse(responseCode = "422", 
                    description = "Error de validación en los campos")
    })
    @PostMapping
    public ResponseEntity<Object> crear(
            @Parameter(description = "Datos del análisis a crear", required = true)
            @Valid @RequestBody AnalisisCreateDTO createDTO) {
        try {
            AnalisisDTO nuevoAnalisis = analisisService.crear(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAnalisis);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Actualizar análisis", 
               description = "Actualiza un análisis existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Análisis actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Datos inválidos o código duplicado"),
        @ApiResponse(responseCode = "404", 
                    description = "Análisis no encontrado"),
        @ApiResponse(responseCode = "422", 
                    description = "Error de validación en los campos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> actualizar(
            @Parameter(description = "ID único del análisis", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Datos actualizados del análisis", required = true)
            @Valid @RequestBody AnalisisCreateDTO updateDTO) {
        try {
            Optional<AnalisisDTO> analisisActualizado = analisisService.actualizar(id, updateDTO);
            return analisisActualizado.map(analisis -> ResponseEntity.ok((Object) analisis))
                                    .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar análisis", 
               description = "Elimina un análisis del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", 
                    description = "Análisis eliminado exitosamente"),
        @ApiResponse(responseCode = "404", 
                    description = "Análisis no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del análisis", required = true)
            @PathVariable UUID id) {
        boolean eliminado = analisisService.eliminar(id);
        return eliminado ? ResponseEntity.noContent().build() 
                        : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Cambiar estado de análisis", 
               description = "Cambia el estado de un análisis (Activo, Inactivo, etc.)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Estado cambiado exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AnalisisDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Análisis no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<AnalisisDTO> cambiarEstado(
            @Parameter(description = "ID único del análisis", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Nuevo estado del análisis", required = true)
            @RequestParam String estado) {
        Optional<AnalisisDTO> analisisActualizado = analisisService.cambiarEstado(id, estado);
        return analisisActualizado.map(ResponseEntity::ok)
                                 .orElse(ResponseEntity.notFound().build());
    }
}