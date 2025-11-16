package com.labflow.service;

import com.labflow.dto.RolCreateDTO;
import com.labflow.dto.RolDTO;
import com.labflow.model.Rol;
import com.labflow.repository.RolRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;

    /**
     * Crear nuevo rol
     */
    @Transactional
    public RolDTO crearRol(RolCreateDTO dto) {
        // Validar que no exista un rol con el mismo nombre
        if (rolRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + dto.getNombre());
        }

        Rol rol = new Rol();
        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        rol.setPermisos(dto.getPermisos());
        rol.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        Rol rolGuardado = rolRepository.save(rol);
        return convertirADTO(rolGuardado);
    }

    /**
     * Obtener rol por ID
     */
    @Transactional(readOnly = true)
    public RolDTO obtenerPorId(UUID id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));
        return convertirADTO(rol);
    }

    /**
     * Obtener rol por nombre
     */
    @Transactional(readOnly = true)
    public RolDTO obtenerPorNombre(String nombre) {
        Rol rol = rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con nombre: " + nombre));
        return convertirADTO(rol);
    }

    /**
     * Listar todos los roles
     */
    @Transactional(readOnly = true)
    public List<RolDTO> listarTodos() {
        return rolRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Listar roles activos
     */
    @Transactional(readOnly = true)
    public List<RolDTO> listarActivos() {
        return rolRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Actualizar rol
     */
    @Transactional
    public RolDTO actualizarRol(UUID id, RolCreateDTO dto) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // Verificar si se está cambiando el nombre y si ya existe
        if (!rol.getNombre().equals(dto.getNombre()) && rolRepository.existsByNombre(dto.getNombre())) {
            throw new IllegalArgumentException("Ya existe un rol con el nombre: " + dto.getNombre());
        }

        rol.setNombre(dto.getNombre());
        rol.setDescripcion(dto.getDescripcion());
        rol.setPermisos(dto.getPermisos());
        if (dto.getActivo() != null) {
            rol.setActivo(dto.getActivo());
        }

        Rol rolActualizado = rolRepository.save(rol);
        return convertirADTO(rolActualizado);
    }

    /**
     * Cambiar estado de rol
     */
    @Transactional
    public RolDTO cambiarEstado(UUID id, Boolean activo) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // Validar que no se desactive un rol crítico si tiene usuarios
        if (!activo && ("ADMINISTRADOR".equals(rol.getNombre()) || "TRABAJADOR".equals(rol.getNombre()))) {
            long cantidadUsuarios = rolRepository.contarUsuariosPorRol(id);
            if (cantidadUsuarios > 0) {
                throw new IllegalStateException("No se puede desactivar el rol " + rol.getNombre() + 
                        " porque tiene " + cantidadUsuarios + " usuario(s) asignado(s)");
            }
        }

        rol.setActivo(activo);
        Rol rolActualizado = rolRepository.save(rol);
        return convertirADTO(rolActualizado);
    }

    /**
     * Eliminar rol
     */
    @Transactional
    public void eliminarRol(UUID id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rol no encontrado con ID: " + id));

        // No permitir eliminar roles del sistema
        if ("ADMINISTRADOR".equals(rol.getNombre()) || "TRABAJADOR".equals(rol.getNombre())) {
            throw new IllegalStateException("No se puede eliminar un rol del sistema");
        }

        // Verificar que no tenga usuarios asignados
        long cantidadUsuarios = rolRepository.contarUsuariosPorRol(id);
        if (cantidadUsuarios > 0) {
            throw new IllegalStateException("No se puede eliminar el rol porque tiene " + 
                    cantidadUsuarios + " usuario(s) asignado(s)");
        }

        rolRepository.delete(rol);
    }

    /**
     * Buscar roles por texto
     */
    @Transactional(readOnly = true)
    public List<RolDTO> buscarPorTexto(String texto) {
        return rolRepository.buscarPorNombre(texto).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Convertir entidad a DTO
     */
    private RolDTO convertirADTO(Rol rol) {
        long cantidadUsuarios = rolRepository.contarUsuariosPorRol(rol.getId());
        
        RolDTO dto = new RolDTO();
        dto.setId(rol.getId());
        dto.setNombre(rol.getNombre());
        dto.setDescripcion(rol.getDescripcion());
        dto.setPermisos(rol.getPermisos());
        dto.setActivo(rol.getActivo());
        dto.setCantidadUsuarios(cantidadUsuarios);
        dto.setCreatedAt(rol.getCreatedAt());
        dto.setUpdatedAt(rol.getUpdatedAt());
        return dto;
    }
}
