package com.labflow.controller;

import com.labflow.dto.ClienteCreateDTO;
import com.labflow.dto.ClienteDTO;
import com.labflow.service.ClienteService;
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
 * Controlador REST para gestionar clientes
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestionar clientes del sistema")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(
        summary = "Crear un nuevo cliente",
        description = "Crea un nuevo cliente en el sistema con el nombre proporcionado"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Cliente creado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content
        )
    })
    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(
            @Valid @RequestBody ClienteCreateDTO clienteCreateDTO) {
        ClienteDTO nuevoCliente = clienteService.crearCliente(clienteCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
    }

    @Operation(
        summary = "Obtener todos los clientes",
        description = "Recupera una lista de todos los clientes registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de clientes recuperada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDTO.class)
            )
        )
    })
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.obtenerTodosLosClientes();
        return ResponseEntity.ok(clientes);
    }

    @Operation(
        summary = "Obtener cliente por ID",
        description = "Recupera un cliente específico usando su ID único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(
            @Parameter(description = "ID único del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        Optional<ClienteDTO> cliente = clienteService.obtenerClientePorId(id);
        return cliente.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Actualizar cliente",
        description = "Actualiza la información de un cliente existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Cliente actualizado exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente no encontrado",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(
            @Parameter(description = "ID único del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody ClienteCreateDTO clienteCreateDTO) {
        Optional<ClienteDTO> clienteActualizado = clienteService.actualizarCliente(id, clienteCreateDTO);
        return clienteActualizado.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Eliminar cliente",
        description = "Elimina un cliente del sistema usando su ID único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Cliente eliminado exitosamente",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Cliente no encontrado",
            content = @Content
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "ID único del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id) {
        boolean eliminado = clienteService.eliminarCliente(id);
        return eliminado ? ResponseEntity.noContent().build() 
                        : ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Buscar clientes por nombre",
        description = "Busca clientes que contengan el texto especificado en su nombre (búsqueda insensible a mayúsculas)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Búsqueda completada exitosamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ClienteDTO.class)
            )
        )
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<ClienteDTO>> buscarClientesPorNombre(
            @Parameter(description = "Texto a buscar en el nombre del cliente", example = "ABC")
            @RequestParam String nombre) {
        List<ClienteDTO> clientes = clienteService.buscarClientesPorNombre(nombre);
        return ResponseEntity.ok(clientes);
    }
}