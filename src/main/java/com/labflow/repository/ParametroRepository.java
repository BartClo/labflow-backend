package com.labflow.repository;

import com.labflow.model.Parametro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para gestionar los parámetros de análisis
 */
@Repository
public interface ParametroRepository extends JpaRepository<Parametro, UUID> {

    /**
     * Busca todos los parámetros de un análisis específico
     */
    @Query("SELECT p FROM Parametro p WHERE p.analisis.idAnalisis = :analisisId")
    List<Parametro> findByAnalisisId(@Param("analisisId") UUID analisisId);

    /**
     * Busca parámetros por nombre (búsqueda parcial, case-insensitive)
     */
    @Query("SELECT p FROM Parametro p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<Parametro> findByNombreContaining(@Param("nombre") String nombre);

    /**
     * Busca un parámetro específico de un análisis por nombre
     */
    @Query("SELECT p FROM Parametro p WHERE p.analisis.idAnalisis = :analisisId AND p.nombre = :nombre")
    Parametro findByAnalisisIdAndNombre(@Param("analisisId") UUID analisisId, @Param("nombre") String nombre);

    /**
     * Cuenta cuántos parámetros tiene un análisis
     */
    @Query("SELECT COUNT(p) FROM Parametro p WHERE p.analisis.idAnalisis = :analisisId")
    long countByAnalisisId(@Param("analisisId") UUID analisisId);
}
