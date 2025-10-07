package com.labflow.repository;

import com.labflow.model.Analisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para operaciones CRUD de la entidad Analisis
 */
@Repository
public interface AnalisisRepository extends JpaRepository<Analisis, UUID> {

    /**
     * Busca un análisis por su código único
     */
    Optional<Analisis> findByCodigo(String codigo);

    /**
     * Busca análisis por categoría
     */
    List<Analisis> findByCategoria(String categoria);

    /**
     * Busca análisis por estado
     */
    List<Analisis> findByEstado(String estado);

    /**
     * Busca análisis activos
     */
    @Query("SELECT a FROM Analisis a WHERE a.estado = 'Activo'")
    List<Analisis> findAllActivos();

    /**
     * Busca análisis por nombre (contiene)
     */
    List<Analisis> findByNombreAnalisisContainingIgnoreCase(String nombre);

    /**
     * Busca análisis por método de ensayo
     */
    List<Analisis> findByMetodoEnsayoContainingIgnoreCase(String metodoEnsayo);

    /**
     * Busca análisis por categoría y estado
     */
    List<Analisis> findByCategoriaAndEstado(String categoria, String estado);

    /**
     * Verifica si existe un análisis con un código específico
     */
    boolean existsByCodigo(String codigo);

    /**
     * Verifica si existe un análisis con un código específico excluyendo un ID
     */
    @Query("SELECT COUNT(a) > 0 FROM Analisis a WHERE a.codigo = :codigo AND a.idAnalisis != :id")
    boolean existsByCodigoAndIdAnalisisNot(@Param("codigo") String codigo, @Param("id") UUID id);
}