package com.labflow.controller;

import com.labflow.dto.RolCreateDTO;
import com.labflow.dto.RolDTO;
import com.labflow.service.RolService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "API para gestión de roles del sistema")
public class RolController {

    private final RolService rolService;

    @PostMapping
    @Operation(summary = "Crear nuevo rol", description = "Crea un nuevo rol en el sistema con sus permisos asociados")
    public ResponseEntity<RolDTO> crearRol(@Valid @RequestBody RolCreateDTO dto) {
        RolDTO rolCreado = rolService.crearRol(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rolCreado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los roles", description = "Obtiene un listado de todos los roles del sistema")
    public ResponseEntity<List<RolDTO>> listarTodos() {
        List<RolDTO> roles = rolService.listarTodos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar roles activos", description = "Obtiene un listado de los roles activos del sistema")
    public ResponseEntity<List<RolDTO>> listarActivos() {
        List<RolDTO> roles = rolService.listarActivos();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rol por ID", description = "Obtiene un rol específico por su identificador único")
    public ResponseEntity<RolDTO> obtenerPorId(
            @Parameter(description = "ID del rol", required = true)
            @PathVariable UUID id) {
        RolDTO rol = rolService.obtenerPorId(id);
        return ResponseEntity.ok(rol);
    }

    @GetMapping("/nombre/{nombre}")
    @Operation(summary = "Obtener rol por nombre", description = "Obtiene un rol específico por su nombre")
    public ResponseEntity<RolDTO> obtenerPorNombre(
            @Parameter(description = "Nombre del rol", required = true)
            @PathVariable String nombre) {
        RolDTO rol = rolService.obtenerPorNombre(nombre);
        return ResponseEntity.ok(rol);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rol", description = "Actualiza la información de un rol existente")
    public ResponseEntity<RolDTO> actualizarRol(
            @Parameter(description = "ID del rol", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody RolCreateDTO dto) {
        RolDTO rolActualizado = rolService.actualizarRol(id, dto);
        return ResponseEntity.ok(rolActualizado);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del rol", description = "Activa o desactiva un rol del sistema")
    public ResponseEntity<RolDTO> cambiarEstado(
            @Parameter(description = "ID del rol", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Nuevo estado (true = activo, false = inactivo)", required = true)
            @RequestParam Boolean activo) {
        RolDTO rolActualizado = rolService.cambiarEstado(id, activo);
        return ResponseEntity.ok(rolActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar rol", description = "Elimina un rol del sistema (solo si no tiene usuarios asignados)")
    public ResponseEntity<Void> eliminarRol(
            @Parameter(description = "ID del rol", required = true)
            @PathVariable UUID id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar roles por texto", description = "Busca roles que contengan el texto especificado en su nombre")
    public ResponseEntity<List<RolDTO>> buscarPorTexto(
            @Parameter(description = "Texto a buscar", required = true)
            @RequestParam String texto) {
        List<RolDTO> roles = rolService.buscarPorTexto(texto);
        return ResponseEntity.ok(roles);
    }
}
