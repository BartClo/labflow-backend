package com.labflow.controller;

import com.labflow.dto.ParametroCreateDTO;
import com.labflow.dto.ParametroDTO;
import com.labflow.service.ParametroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gestionar parámetros de análisis
 */
@RestController
@RequestMapping("/api/parametros")
@Tag(name = "Parámetros", description = "API para gestionar parámetros de medición de análisis")
public class ParametroController {

    private final ParametroService parametroService;

    @Autowired
    public ParametroController(ParametroService parametroService) {
        this.parametroService = parametroService;
    }

    /**
     * Obtiene todos los parámetros
     */
    @GetMapping
    @Operation(summary = "Listar todos los parámetros", 
               description = "Obtiene una lista de todos los parámetros de análisis registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ParametroDTO>> obtenerTodos() {
        List<ParametroDTO> parametros = parametroService.obtenerTodos();
        return ResponseEntity.ok(parametros);
    }

    /**
     * Obtiene un parámetro por su ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener parámetro por ID", 
               description = "Obtiene los detalles de un parámetro específico mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parámetro encontrado"),
        @ApiResponse(responseCode = "404", description = "Parámetro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ParametroDTO> obtenerPorId(
            @Parameter(description = "ID único del parámetro", required = true)
            @PathVariable UUID id) {
        return parametroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene todos los parámetros de un análisis específico
     */
    @GetMapping("/analisis/{idAnalisis}")
    @Operation(summary = "Listar parámetros por análisis", 
               description = "Obtiene todos los parámetros asociados a un análisis específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ParametroDTO>> obtenerPorAnalisisId(
            @Parameter(description = "ID del análisis", required = true)
            @PathVariable UUID idAnalisis) {
        List<ParametroDTO> parametros = parametroService.obtenerPorAnalisisId(idAnalisis);
        return ResponseEntity.ok(parametros);
    }

    /**
     * Busca parámetros por nombre
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar parámetros por nombre", 
               description = "Busca parámetros cuyo nombre contenga el texto especificado (búsqueda parcial, insensible a mayúsculas)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<ParametroDTO>> buscarPorNombre(
            @Parameter(description = "Texto a buscar en el nombre del parámetro", required = true)
            @RequestParam String nombre) {
        List<ParametroDTO> parametros = parametroService.buscarPorNombre(nombre);
        return ResponseEntity.ok(parametros);
    }

    /**
     * Obtiene un parámetro específico de un análisis por nombre
     */
    @GetMapping("/analisis/{idAnalisis}/nombre/{nombre}")
    @Operation(summary = "Obtener parámetro por análisis y nombre", 
               description = "Obtiene un parámetro específico de un análisis mediante su nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parámetro encontrado"),
        @ApiResponse(responseCode = "404", description = "Parámetro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ParametroDTO> obtenerPorAnalisisIdYNombre(
            @Parameter(description = "ID del análisis", required = true)
            @PathVariable UUID idAnalisis,
            @Parameter(description = "Nombre del parámetro", required = true)
            @PathVariable String nombre) {
        return parametroService.obtenerPorAnalisisIdYNombre(idAnalisis, nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Cuenta los parámetros de un análisis
     */
    @GetMapping("/analisis/{idAnalisis}/contar")
    @Operation(summary = "Contar parámetros de un análisis", 
               description = "Obtiene el número total de parámetros asociados a un análisis")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Conteo realizado exitosamente"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Long> contarPorAnalisisId(
            @Parameter(description = "ID del análisis", required = true)
            @PathVariable UUID idAnalisis) {
        long cantidad = parametroService.contarPorAnalisisId(idAnalisis);
        return ResponseEntity.ok(cantidad);
    }

    /**
     * Crea un nuevo parámetro
     */
    @PostMapping
    @Operation(summary = "Crear nuevo parámetro", 
               description = "Crea un nuevo parámetro de medición para un análisis con sus configuraciones de control de calidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Parámetro creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ParametroDTO> crear(
            @Parameter(description = "Datos del nuevo parámetro", required = true)
            @Valid @RequestBody ParametroCreateDTO createDTO) {
        try {
            ParametroDTO parametroCreado = parametroService.crear(createDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(parametroCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Actualiza un parámetro existente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar parámetro", 
               description = "Actualiza los datos de un parámetro existente, incluyendo sus configuraciones de control de calidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Parámetro actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Parámetro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ParametroDTO> actualizar(
            @Parameter(description = "ID del parámetro a actualizar", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Nuevos datos del parámetro", required = true)
            @Valid @RequestBody ParametroCreateDTO updateDTO) {
        try {
            return parametroService.actualizar(id, updateDTO)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un parámetro
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar parámetro", 
               description = "Elimina un parámetro y todas sus configuraciones de control de calidad asociadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Parámetro eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Parámetro no encontrado"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del parámetro a eliminar", required = true)
            @PathVariable UUID id) {
        if (parametroService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
