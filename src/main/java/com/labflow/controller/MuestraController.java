package com.labflow.controller;

import com.labflow.dto.ActualizarPrioridadDTO;
import com.labflow.dto.MuestraCreateDTO;
import com.labflow.dto.MuestraDTO;
import com.labflow.dto.MuestraPageResponse;
import com.labflow.service.MuestraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST para la gestión de muestras de laboratorio
 * Proporciona endpoints para CRUD completo y operaciones avanzadas
 */
@RestController
@RequestMapping("/api/muestras")
@Tag(name = "Muestras", description = "API para gestión de muestras de laboratorio")
@CrossOrigin(origins = "*")
public class MuestraController {

    private final MuestraService muestraService;

    public MuestraController(MuestraService muestraService) {
        this.muestraService = muestraService;
    }

    @Operation(summary = "Crear nueva muestra", 
               description = "Crea una nueva muestra con sus análisis asociados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Muestra creada exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = MuestraDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente o análisis no encontrado"),
        @ApiResponse(responseCode = "409", description = "Conflicto - número interno o código de barras duplicado")
    })
    @PostMapping
    public ResponseEntity<MuestraDTO> crearMuestra(
            @Valid @RequestBody MuestraCreateDTO createDTO) {
        MuestraDTO muestra = muestraService.crearMuestra(createDTO);
        return new ResponseEntity<>(muestra, HttpStatus.OK);
    }

    @Operation(summary = "Obtener muestra por ID", 
               description = "Recupera una muestra específica por su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Muestra encontrada",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = MuestraDTO.class))),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MuestraDTO> obtenerMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        MuestraDTO muestra = muestraService.obtenerMuestraPorId(id);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Obtener muestra por número interno", 
               description = "Recupera una muestra usando su número interno único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Muestra encontrada"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada")
    })
    @GetMapping("/numero-interno/{numeroInterno}")
    public ResponseEntity<MuestraDTO> obtenerMuestraPorNumeroInterno(
            @Parameter(description = "Número interno de la muestra") 
            @PathVariable String numeroInterno) {
        MuestraDTO muestra = muestraService.obtenerMuestraPorNumeroInterno(numeroInterno);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Listar todas las muestras", 
               description = "Obtiene una lista paginada de todas las muestras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de muestras obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MuestraPageResponse.class)))
    })
    @GetMapping
    public ResponseEntity<Page<MuestraDTO>> listarMuestras(
            @ParameterObject
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<MuestraDTO> muestras = muestraService.listarMuestras(pageable);
        return ResponseEntity.ok(muestras);
    }

    @Operation(summary = "Buscar muestras", 
               description = "Busca muestras usando múltiples criterios de filtrado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MuestraPageResponse.class)))
    })
    @GetMapping("/buscar")
    public ResponseEntity<Page<MuestraDTO>> buscarMuestras(
            @Parameter(description = "ID del cliente") 
            @RequestParam(required = false) UUID clienteId,
            
            @Parameter(description = "Estado de la muestra", 
                      schema = @Schema(allowableValues = {"RECIBIDA", "EN_PROCESO", "ANALIZADA", "COMPLETADA", "RECHAZADA"}))
            @RequestParam(required = false) String estado,
            
            @Parameter(description = "Prioridad de la muestra", 
                      schema = @Schema(allowableValues = {"ALTA", "MEDIA", "BAJA"}))
            @RequestParam(required = false) String prioridad,
            
            @Parameter(description = "Fecha de inicio para filtrar por fecha de muestreo")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            
            @Parameter(description = "Fecha de fin para filtrar por fecha de muestreo")
            @RequestParam(required = false) 
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            
            @ParameterObject
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        Page<MuestraDTO> muestras = muestraService.buscarMuestras(
                clienteId, estado, prioridad, fechaInicio, fechaFin, pageable);
        return ResponseEntity.ok(muestras);
    }

    @Operation(summary = "Búsqueda de texto libre", 
               description = "Busca muestras por texto en campos principales")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = MuestraPageResponse.class)))
    })
    @GetMapping("/buscar-texto")
    public ResponseEntity<Page<MuestraDTO>> buscarPorTexto(
            @Parameter(description = "Texto a buscar en los campos de la muestra")
            @RequestParam String texto,
            
            @ParameterObject
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        
        Page<MuestraDTO> muestras = muestraService.buscarPorTexto(texto, pageable);
        return ResponseEntity.ok(muestras);
    }

    @Operation(summary = "Actualizar muestra", 
               description = "Actualiza una muestra existente con nuevos datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Muestra actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada"),
        @ApiResponse(responseCode = "409", description = "Conflicto - número interno o código de barras duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MuestraDTO> actualizarMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            @Valid @RequestBody MuestraCreateDTO updateDTO) {
        MuestraDTO muestra = muestraService.actualizarMuestra(id, updateDTO);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Cambiar estado de muestra", 
               description = "Actualiza el estado de una muestra específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido o transición no permitida"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<MuestraDTO> cambiarEstado(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            
            @Parameter(description = "Nuevo estado de la muestra", 
                      schema = @Schema(allowableValues = {"RECIBIDA", "EN_PROCESO", "ANALIZADA", "COMPLETADA", "RECHAZADA"}))
            @RequestParam String estado) {
        MuestraDTO muestra = muestraService.cambiarEstado(id, estado);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Cambiar prioridad de muestra",
               description = "Actualiza la prioridad de una muestra específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prioridad actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Prioridad inválida"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada")
    })
    @PatchMapping("/{id}/prioridad")
    public ResponseEntity<MuestraDTO> cambiarPrioridad(
            @Parameter(description = "ID único de la muestra")
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarPrioridadDTO dto) {
        MuestraDTO muestra = muestraService.cambiarPrioridad(id, dto.getPrioridad());
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Agregar análisis a muestra", 
               description = "Agrega nuevos análisis a una muestra existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Análisis agregados exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra o análisis no encontrado"),
        @ApiResponse(responseCode = "409", description = "Análisis ya asociado a la muestra")
    })
    @PostMapping("/{id}/analisis")
    public ResponseEntity<MuestraDTO> agregarAnalisis(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            
            @Parameter(description = "Lista de IDs de análisis a agregar")
            @RequestBody List<UUID> analisisIds) {
        MuestraDTO muestra = muestraService.agregarAnalisis(id, analisisIds);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Remover análisis de muestra", 
               description = "Remueve un análisis específico de una muestra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Análisis removido exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra o relación no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede remover análisis en proceso")
    })
    @DeleteMapping("/{muestraId}/analisis/{analisisId}")
    public ResponseEntity<MuestraDTO> removerAnalisis(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID muestraId,
            
            @Parameter(description = "ID único del análisis") 
            @PathVariable UUID analisisId) {
        MuestraDTO muestra = muestraService.removerAnalisis(muestraId, analisisId);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Eliminar muestra", 
               description = "Elimina una muestra del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Muestra eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada"),
        @ApiResponse(responseCode = "400", description = "No se puede eliminar muestra con análisis en proceso")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        muestraService.eliminarMuestra(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener muestras de cliente", 
               description = "Obtiene todas las muestras asociadas a un cliente específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Muestras del cliente obtenidas exitosamente",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MuestraPageResponse.class)))
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<MuestraDTO>> obtenerMuestrasCliente(
            @Parameter(description = "ID único del cliente") 
            @PathVariable UUID clienteId,
            
            @ParameterObject
            @PageableDefault(size = 20, sort = "fechaCreacion", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<MuestraDTO> muestras = muestraService.obtenerMuestrasCliente(clienteId, pageable);
        return ResponseEntity.ok(muestras);
    }

    @Operation(summary = "Obtener estadísticas de muestras", 
               description = "Proporciona estadísticas generales del sistema de muestras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente")
    })
    @GetMapping("/estadisticas")
    public ResponseEntity<Map<String, Object>> obtenerEstadisticas() {
        Map<String, Object> estadisticas = muestraService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }

    @Operation(summary = "Obtener cola de trabajo del laboratorio", 
               description = "Obtiene las muestras en cola para procesamiento en el laboratorio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cola de trabajo obtenida exitosamente")
    })
    @GetMapping("/cola-laboratorio")
    public ResponseEntity<List<MuestraDTO>> obtenerColaLaboratorio(
            @Parameter(description = "Límite de muestras a retornar (por defecto 50)")
            @RequestParam(defaultValue = "50") int limite) {
        List<MuestraDTO> cola = muestraService.obtenerColaLaboratorio(limite);
        return ResponseEntity.ok(cola);
    }

    // Endpoints adicionales para operaciones específicas del laboratorio

    @Operation(summary = "Recibir muestra", 
               description = "Marca una muestra como recibida en el laboratorio")
    @PostMapping("/{id}/recibir")
    public ResponseEntity<MuestraDTO> recibirMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        MuestraDTO muestra = muestraService.cambiarEstado(id, "RECIBIDA");
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Iniciar procesamiento", 
               description = "Marca una muestra como en proceso")
    @PostMapping("/{id}/procesar")
    public ResponseEntity<MuestraDTO> procesarMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        MuestraDTO muestra = muestraService.cambiarEstado(id, "EN_PROCESO");
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Completar muestra", 
               description = "Marca una muestra como completada")
    @PostMapping("/{id}/completar")
    public ResponseEntity<MuestraDTO> completarMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        MuestraDTO muestra = muestraService.cambiarEstado(id, "COMPLETADA");
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Rechazar muestra", 
               description = "Marca una muestra como rechazada")
    @PostMapping("/{id}/rechazar")
    public ResponseEntity<MuestraDTO> rechazarMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        MuestraDTO muestra = muestraService.cambiarEstado(id, "RECHAZADA");
        return ResponseEntity.ok(muestra);
    }

    // ================= ENDPOINTS PARA GESTIÓN DE PLANTILLAS =================

    @Operation(summary = "Agregar plantillas a muestra", 
               description = "Agrega una o más plantillas a una muestra existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plantillas agregadas exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/{id}/plantillas")
    public ResponseEntity<MuestraDTO> agregarPlantillasAMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            @Parameter(description = "Lista de IDs de plantillas a agregar")
            @RequestBody List<UUID> plantillaIds) {
        MuestraDTO muestra = muestraService.agregarPlantillasAMuestra(id, plantillaIds);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Remover plantilla de muestra", 
               description = "Remueve una plantilla específica de una muestra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Plantilla removida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra o plantilla no encontrada")
    })
    @DeleteMapping("/{id}/plantillas/{plantillaId}")
    public ResponseEntity<MuestraDTO> removerPlantillaDeMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            @Parameter(description = "ID único de la plantilla a remover") 
            @PathVariable UUID plantillaId) {
        MuestraDTO muestra = muestraService.removerPlantillaDeMuestra(id, plantillaId);
        return ResponseEntity.ok(muestra);
    }

    @Operation(summary = "Obtener plantillas de muestra", 
               description = "Obtiene todas las plantillas asociadas a una muestra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de plantillas obtenida exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra no encontrada")
    })
    @GetMapping("/{id}/plantillas")
    public ResponseEntity<List<com.labflow.model.MuestraPlantilla>> obtenerPlantillasDeMuestra(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id) {
        List<com.labflow.model.MuestraPlantilla> plantillas = muestraService.obtenerPlantillasDeMuestra(id);
        return ResponseEntity.ok(plantillas);
    }

    @Operation(summary = "Actualizar estado de plantilla", 
               description = "Actualiza el estado de una plantilla específica en una muestra")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Muestra o plantilla no encontrada"),
        @ApiResponse(responseCode = "400", description = "Estado inválido")
    })
    @PutMapping("/{id}/plantillas/{plantillaId}/estado")
    public ResponseEntity<com.labflow.model.MuestraPlantilla> actualizarEstadoPlantilla(
            @Parameter(description = "ID único de la muestra") 
            @PathVariable UUID id,
            @Parameter(description = "ID único de la plantilla") 
            @PathVariable UUID plantillaId,
            @Parameter(description = "Nuevo estado de la plantilla") 
            @RequestParam String estado) {
        
        com.labflow.model.MuestraPlantilla.EstadoPlantilla nuevoEstado;
        try {
            nuevoEstado = com.labflow.model.MuestraPlantilla.EstadoPlantilla.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido. Valores permitidos: PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA");
        }
        
        com.labflow.model.MuestraPlantilla plantillaActualizada = 
            muestraService.actualizarEstadoPlantilla(id, plantillaId, nuevoEstado);
        return ResponseEntity.ok(plantillaActualizada);
    }
}