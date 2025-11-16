package com.labflow.service;

import com.labflow.dto.RolDTO;
import com.labflow.dto.UsuarioCreateDTO;
import com.labflow.dto.UsuarioDTO;
import com.labflow.model.Rol;
import com.labflow.model.Usuario;
import com.labflow.repository.RolRepository;
import com.labflow.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    /**
     * Crear nuevo usuario
     */
    @Transactional
    public UsuarioDTO crearUsuario(UsuarioCreateDTO dto) {
        // Validar que no exista email duplicado
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        // Validar que no exista username duplicado
        if (usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + dto.getUsername());
        }

        // Buscar el rol
        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + dto.getRolId()));

        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword()); // TODO: En producción, encriptar con BCrypt
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        usuario.setRol(rol);
        usuario.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    /**
     * Obtener usuario por ID
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return convertirADTO(usuario);
    }

    /**
     * Obtener usuario por email
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }

    /**
     * Obtener usuario por username
     */
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con username: " + username));
        return convertirADTO(usuario);
    }

    /**
     * Listar todos los usuarios
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar usuarios con paginación
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> listarConPaginacion(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(this::convertirADTO);
    }

    /**
     * Listar usuarios activos
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarActivos() {
        return usuarioRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar usuarios por rol
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarPorRol(UUID rolId) {
        return usuarioRepository.findByRolId(rolId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar administradores
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarAdministradores() {
        return usuarioRepository.findAdministradores().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar trabajadores
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> listarTrabajadores() {
        return usuarioRepository.findTrabajadores().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar usuario
     */
    @Transactional
    public UsuarioDTO actualizarUsuario(UUID id, UsuarioCreateDTO dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        // Validar email si cambió
        if (!usuario.getEmail().equals(dto.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + dto.getEmail());
        }

        // Validar username si cambió
        if (!usuario.getUsername().equals(dto.getUsername()) && usuarioRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Ya existe un usuario con el username: " + dto.getUsername());
        }

        // Buscar el rol si cambió
        if (!usuario.getRol().getId().equals(dto.getRolId())) {
            Rol rol = rolRepository.findById(dto.getRolId())
                    .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + dto.getRolId()));
            usuario.setRol(rol);
        }

        // Actualizar datos
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(dto.getPassword()); // TODO: Encriptar
        }
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Cambiar estado del usuario
     */
    @Transactional
    public UsuarioDTO cambiarEstado(UUID id, Boolean activo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(activo);
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Registrar conexión de usuario
     */
    @Transactional
    public UsuarioDTO registrarConexion(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuario.registrarConexion();
        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return convertirADTO(usuarioActualizado);
    }

    /**
     * Eliminar usuario
     */
    @Transactional
    public void eliminarUsuario(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuarioRepository.delete(usuario);
    }

    /**
     * Buscar usuarios por texto
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> buscarPorTexto(String texto) {
        return usuarioRepository.buscarPorTexto(texto).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Buscar usuarios por texto con paginación
     */
    @Transactional(readOnly = true)
    public Page<UsuarioDTO> buscarPorTextoConPaginacion(String texto, Pageable pageable) {
        return usuarioRepository.buscarPorTexto(texto, pageable)
                .map(this::convertirADTO);
    }

    /**
     * Obtener estadísticas de usuarios
     */
    @Transactional(readOnly = true)
    public UsuarioEstadisticas obtenerEstadisticas() {
        long total = usuarioRepository.count();
        long activos = usuarioRepository.countByActivoTrue();
        long administradores = usuarioRepository.findAdministradores().size();
        long trabajadores = usuarioRepository.findTrabajadores().size();

        return new UsuarioEstadisticas(total, activos, administradores, trabajadores);
    }

    /**
     * Convertir entidad a DTO
     */
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setEmail(usuario.getEmail());
        dto.setUsername(usuario.getUsername());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setActivo(usuario.getActivo());
        dto.setUltimaConexion(usuario.getUltimaConexion());
        dto.setCreatedAt(usuario.getCreatedAt());
        dto.setUpdatedAt(usuario.getUpdatedAt());

        // Convertir rol a DTO simplificado
        if (usuario.getRol() != null) {
            RolDTO rolDTO = new RolDTO();
            rolDTO.setId(usuario.getRol().getId());
            rolDTO.setNombre(usuario.getRol().getNombre());
            rolDTO.setDescripcion(usuario.getRol().getDescripcion());
            rolDTO.setPermisos(usuario.getRol().getPermisos());
            rolDTO.setActivo(usuario.getRol().getActivo());
            dto.setRol(rolDTO);
        }

        return dto;
    }

    /**
     * Clase interna para estadísticas
     */
    public record UsuarioEstadisticas(
            long totalUsuarios,
            long usuariosActivos,
            long administradores,
            long trabajadores
    ) {}
}
