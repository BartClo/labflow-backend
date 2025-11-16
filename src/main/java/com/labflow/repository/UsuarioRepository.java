package com.labflow.repository;

import com.labflow.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    /**
     * Buscar usuario por email
     */
    Optional<Usuario> findByEmail(String email);

    /**
     * Buscar usuario por username
     */
    Optional<Usuario> findByUsername(String username);

    /**
     * Verificar si existe un email
     */
    boolean existsByEmail(String email);

    /**
     * Verificar si existe un username
     */
    boolean existsByUsername(String username);

    /**
     * Listar todos los usuarios activos
     */
    List<Usuario> findByActivoTrue();

    /**
     * Listar todos los usuarios inactivos
     */
    List<Usuario> findByActivoFalse();

    /**
     * Listar usuarios por rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.id = :rolId")
    List<Usuario> findByRolId(@Param("rolId") UUID rolId);

    /**
     * Listar usuarios por nombre de rol
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :nombreRol")
    List<Usuario> findByRolNombre(@Param("nombreRol") String nombreRol);

    /**
     * Buscar usuarios con paginación
     */
    @NonNull
    Page<Usuario> findAll(@NonNull Pageable pageable);

    /**
     * Buscar usuarios activos con paginación
     */
    Page<Usuario> findByActivoTrue(Pageable pageable);

    /**
     * Búsqueda de texto libre en nombre, apellido, email o username
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Usuario> buscarPorTexto(@Param("texto") String texto);

    /**
     * Búsqueda de texto libre con paginación
     */
    @Query("SELECT u FROM Usuario u WHERE " +
           "LOWER(u.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.apellido) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(u.username) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Usuario> buscarPorTexto(@Param("texto") String texto, Pageable pageable);

    /**
     * Buscar usuarios que no han iniciado sesión desde una fecha
     */
    @Query("SELECT u FROM Usuario u WHERE u.ultimaConexion < :fecha OR u.ultimaConexion IS NULL")
    List<Usuario> findUsuariosInactivosDesdeFecha(@Param("fecha") LocalDateTime fecha);

    /**
     * Contar usuarios por rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.id = :rolId")
    long contarPorRol(@Param("rolId") UUID rolId);

    /**
     * Contar usuarios activos
     */
    long countByActivoTrue();

    /**
     * Buscar usuarios administradores
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'ADMINISTRADOR'")
    List<Usuario> findAdministradores();

    /**
     * Buscar usuarios trabajadores
     */
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = 'TRABAJADOR'")
    List<Usuario> findTrabajadores();
}
