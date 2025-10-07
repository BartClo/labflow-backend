package com.labflow.repository;

import com.labflow.model.PlantillaAnalisis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para operaciones CRUD de la entidad PlantillaAnalisis
 */
@Repository
public interface PlantillaAnalisisRepository extends JpaRepository<PlantillaAnalisis, UUID> {

    /**
     * Busca análisis de una plantilla específica ordenados por orden de ejecución
     */
    List<PlantillaAnalisis> findByPlantillaIdPlantillaOrderByOrdenEnPlantillaAsc(UUID idPlantilla);

    /**
     * Busca plantillas que contengan un análisis específico
     */
    List<PlantillaAnalisis> findByAnalisisIdAnalisis(UUID idAnalisis);

    /**
     * Elimina todas las relaciones de una plantilla específica
     */
    void deleteByPlantillaIdPlantilla(UUID idPlantilla);

    /**
     * Elimina una relación específica entre plantilla y análisis
     */
    void deleteByPlantillaIdPlantillaAndAnalisisIdAnalisis(UUID idPlantilla, UUID idAnalisis);

    /**
     * Verifica si existe una relación entre plantilla y análisis
     */
    boolean existsByPlantillaIdPlantillaAndAnalisisIdAnalisis(UUID idPlantilla, UUID idAnalisis);

    /**
     * Cuenta cuántos análisis tiene una plantilla
     */
    @Query("SELECT COUNT(pa) FROM PlantillaAnalisis pa WHERE pa.plantilla.idPlantilla = :idPlantilla")
    Long countByPlantillaId(@Param("idPlantilla") UUID idPlantilla);

    /**
     * Obtiene el máximo orden en una plantilla
     */
    @Query("SELECT COALESCE(MAX(pa.ordenEnPlantilla), 0) FROM PlantillaAnalisis pa WHERE pa.plantilla.idPlantilla = :idPlantilla")
    Integer findMaxOrdenByPlantillaId(@Param("idPlantilla") UUID idPlantilla);
}