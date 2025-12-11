package com.labflow.controller;

import com.labflow.dto.LoginRequestDTO;
import com.labflow.dto.LoginResponseDTO;
import com.labflow.dto.UsuarioCreateDTO;
import com.labflow.dto.UsuarioDTO;
import com.labflow.service.UsuarioService;
import com.labflow.service.UsuarioService.UsuarioEstadisticas;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "API para gestión de usuarios del sistema")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida las credenciales de un usuario y retorna su información con el rol asignado")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        LoginResponseDTO response = usuarioService.login(loginRequest);
        
        if (Boolean.TRUE.equals(response.getSuccess())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping
    @Operation(summary = "Crear nuevo usuario", description = "Crea un nuevo usuario en el sistema con su rol asignado")
    public ResponseEntity<UsuarioDTO> crearUsuario(@Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioCreado);
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene un listado de todos los usuarios del sistema con paginación opcional")
    public ResponseEntity<List<UsuarioDTO>> listarTodos(
            @Parameter(description = "Número de página (opcional)")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Tamaño de página (opcional)")
            @RequestParam(required = false) Integer size,
            @Parameter(description = "Campo para ordenar (opcional)")
            @RequestParam(required = false) String sortBy) {
        
        if (page != null && size != null) {
            Sort sort = sortBy != null ? Sort.by(sortBy) : Sort.by("createdAt").descending();
            Pageable pageable = PageRequest.of(page, size, sort);
            Page<UsuarioDTO> usuarios = usuarioService.listarConPaginacion(pageable);
            return ResponseEntity.ok(usuarios.getContent());
        }
        
        List<UsuarioDTO> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/activos")
    @Operation(summary = "Listar usuarios activos", description = "Obtiene un listado de los usuarios activos del sistema")
    public ResponseEntity<List<UsuarioDTO>> listarActivos() {
        List<UsuarioDTO> usuarios = usuarioService.listarActivos();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su identificador único")
    public ResponseEntity<UsuarioDTO> obtenerPorId(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable UUID id) {
        UsuarioDTO usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener usuario por email", description = "Obtiene un usuario específico por su dirección de email")
    public ResponseEntity<UsuarioDTO> obtenerPorEmail(
            @Parameter(description = "Email del usuario", required = true)
            @PathVariable String email) {
        UsuarioDTO usuario = usuarioService.obtenerPorEmail(email);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Obtener usuario por username", description = "Obtiene un usuario específico por su nombre de usuario")
    public ResponseEntity<UsuarioDTO> obtenerPorUsername(
            @Parameter(description = "Username del usuario", required = true)
            @PathVariable String username) {
        UsuarioDTO usuario = usuarioService.obtenerPorUsername(username);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/rol/{rolId}")
    @Operation(summary = "Listar usuarios por rol", description = "Obtiene un listado de usuarios que tienen un rol específico")
    public ResponseEntity<List<UsuarioDTO>> listarPorRol(
            @Parameter(description = "ID del rol", required = true)
            @PathVariable UUID rolId) {
        List<UsuarioDTO> usuarios = usuarioService.listarPorRol(rolId);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/administradores")
    @Operation(summary = "Listar administradores", description = "Obtiene un listado de todos los usuarios con rol de administrador")
    public ResponseEntity<List<UsuarioDTO>> listarAdministradores() {
        List<UsuarioDTO> usuarios = usuarioService.listarAdministradores();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/trabajadores")
    @Operation(summary = "Listar trabajadores", description = "Obtiene un listado de todos los usuarios con rol de trabajador")
    public ResponseEntity<List<UsuarioDTO>> listarTrabajadores() {
        List<UsuarioDTO> usuarios = usuarioService.listarTrabajadores();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza la información de un usuario existente")
    public ResponseEntity<UsuarioDTO> actualizarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable UUID id,
            @Valid @RequestBody UsuarioCreateDTO dto) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, dto);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del usuario", description = "Activa o desactiva un usuario del sistema")
    public ResponseEntity<UsuarioDTO> cambiarEstado(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Nuevo estado (true = activo, false = inactivo)", required = true)
            @RequestParam Boolean activo) {
        UsuarioDTO usuarioActualizado = usuarioService.cambiarEstado(id, activo);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @PostMapping("/{id}/registrar-conexion")
    @Operation(summary = "Registrar conexión", description = "Registra la última conexión del usuario al sistema")
    public ResponseEntity<UsuarioDTO> registrarConexion(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable UUID id) {
        UsuarioDTO usuarioActualizado = usuarioService.registrarConexion(id);
        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Elimina un usuario del sistema")
    public ResponseEntity<Void> eliminarUsuario(
            @Parameter(description = "ID del usuario", required = true)
            @PathVariable UUID id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar usuarios por texto", description = "Busca usuarios que contengan el texto especificado en nombre, apellido, email o username")
    public ResponseEntity<List<UsuarioDTO>> buscarPorTexto(
            @Parameter(description = "Texto a buscar", required = true)
            @RequestParam String texto,
            @Parameter(description = "Número de página (opcional)")
            @RequestParam(required = false) Integer page,
            @Parameter(description = "Tamaño de página (opcional)")
            @RequestParam(required = false) Integer size) {
        
        if (page != null && size != null) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UsuarioDTO> usuarios = usuarioService.buscarPorTextoConPaginacion(texto, pageable);
            return ResponseEntity.ok(usuarios.getContent());
        }
        
        List<UsuarioDTO> usuarios = usuarioService.buscarPorTexto(texto);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/estadisticas")
    @Operation(summary = "Obtener estadísticas de usuarios", description = "Obtiene estadísticas generales sobre los usuarios del sistema")
    public ResponseEntity<UsuarioEstadisticas> obtenerEstadisticas() {
        UsuarioEstadisticas estadisticas = usuarioService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }
}
