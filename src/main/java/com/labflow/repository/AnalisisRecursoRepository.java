package com.labflow.repository;

import com.labflow.model.AnalisisRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para la entidad AnalisisRecurso
 */
@Repository
public interface AnalisisRecursoRepository extends JpaRepository<AnalisisRecurso, UUID> {

    /**
     * Buscar todos los recursos asociados a un análisis
     */
    List<AnalisisRecurso> findByAnalisis_AnalisisId(UUID analisisId);

    /**
     * Buscar análisis que usan un equipo específico
     */
    List<AnalisisRecurso> findByEquipo_EquipoId(UUID equipoId);

    /**
     * Buscar análisis que usan un reactivo específico
     */
    List<AnalisisRecurso> findByReactivo_ReactivoId(UUID reactivoId);

    /**
     * Buscar análisis que usan un insumo específico
     */
    List<AnalisisRecurso> findByInsumo_InsumoId(UUID insumoId);
}
