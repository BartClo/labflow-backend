package com.labflow.repository;

import com.labflow.model.Plantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para operaciones CRUD de la entidad Plantilla
 */
@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, UUID> {

    /**
     * Busca plantillas por estado
     */
    List<Plantilla> findByEstado(String estado);

    /**
     * Busca plantillas activas
     */
    @Query("SELECT p FROM Plantilla p WHERE p.estado = 'Activo'")
    List<Plantilla> findAllActivas();

    /**
     * Busca plantillas por nombre (contiene)
     */
    List<Plantilla> findByNombrePlantillaContainingIgnoreCase(String nombre);

    /**
     * Busca plantillas que contengan un tipo de muestra específico
     */
    @Query(value = "SELECT * FROM plantillas p WHERE :tipoMuestra = ANY(p.tipos_muestra_aplicables)", nativeQuery = true)
    List<Plantilla> findByTipoMuestra(@Param("tipoMuestra") String tipoMuestra);

    /**
     * Busca plantillas que contengan cualquiera de los tipos de muestra especificados
     */
    @Query(value = "SELECT * FROM plantillas p WHERE p.tipos_muestra_aplicables && CAST(?1 AS text[])", nativeQuery = true)
    List<Plantilla> findByTiposMuestraAplicables(@Param("tiposMuestra") String[] tiposMuestra);

    /**
     * Busca plantillas por estado y tipo de muestra
     */
    @Query(value = "SELECT * FROM plantillas p WHERE p.estado = :estado AND :tipoMuestra = ANY(p.tipos_muestra_aplicables)", nativeQuery = true)
    List<Plantilla> findByEstadoAndTipoMuestra(@Param("estado") String estado, @Param("tipoMuestra") String tipoMuestra);

    /**
     * Verifica si existe una plantilla con un nombre específico
     */
    boolean existsByNombrePlantilla(String nombrePlantilla);

    /**
     * Verifica si existe una plantilla con un nombre específico excluyendo un ID
     */
    @Query("SELECT COUNT(p) > 0 FROM Plantilla p WHERE p.nombrePlantilla = :nombre AND p.idPlantilla != :id")
    boolean existsByNombrePlantillaAndIdPlantillaNot(@Param("nombre") String nombrePlantilla, @Param("id") UUID id);

    /**
     * Obtiene plantillas con la cantidad de análisis incluidos
     */
    @Query("SELECT p, COUNT(pa) as cantidadAnalisis FROM Plantilla p LEFT JOIN p.plantillaAnalisis pa GROUP BY p")
    List<Object[]> findPlantillasWithAnalysisCount();

    /**
     * Busca plantillas que contengan un análisis específico
     */
    @Query("SELECT DISTINCT p FROM Plantilla p JOIN p.plantillaAnalisis pa WHERE pa.analisis.idAnalisis = :analisisId")
    List<Plantilla> findByAnalisisId(@Param("analisisId") UUID analisisId);

    /**
     * Busca plantillas ordenadas por nombre
     */
    List<Plantilla> findAllByOrderByNombrePlantillaAsc();

    /**
     * Busca plantillas activas ordenadas por fecha de creación
     */
    @Query("SELECT p FROM Plantilla p WHERE p.estado = 'Activo' ORDER BY p.fechaCreacion DESC")
    List<Plantilla> findActivasOrderByFechaCreacionDesc();
}