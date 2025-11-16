package com.labflow.repository;

import com.labflow.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RolRepository extends JpaRepository<Rol, UUID> {

    /**
     * Buscar rol por nombre
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Verificar si existe un rol con el nombre dado
     */
    boolean existsByNombre(String nombre);

    /**
     * Listar todos los roles activos
     */
    List<Rol> findByActivoTrue();

    /**
     * Listar todos los roles inactivos
     */
    List<Rol> findByActivoFalse();

    /**
     * Buscar roles por nombre que contenga el texto (case insensitive)
     */
    @Query("SELECT r FROM Rol r WHERE LOWER(r.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Rol> buscarPorNombre(@Param("texto") String texto);

    /**
     * Contar cuántos usuarios tiene un rol
     */
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.rol.id = :rolId")
    long contarUsuariosPorRol(@Param("rolId") UUID rolId);

    /**
     * Buscar roles que tengan un permiso específico
     */
    @Query(value = "SELECT * FROM roles WHERE :permiso = ANY(permisos)", nativeQuery = true)
    List<Rol> findByPermisoContaining(@Param("permiso") String permiso);
}
