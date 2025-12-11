package com.labflow.repository;

import com.labflow.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad Equipo
 */
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, UUID> {

    /**
     * Buscar equipo por nombre exacto
     */
    Optional<Equipo> findByNombre(String nombre);

    /**
     * Buscar equipo por código
     */
    Optional<Equipo> findByCodigo(String codigo);

    /**
     * Listar equipos activos
     */
    List<Equipo> findByActivoTrue();

    /**
     * Buscar equipos por marca
     */
    List<Equipo> findByMarcaContainingIgnoreCaseAndActivoTrue(String marca);
}
