package com.labflow.repository;

import com.labflow.model.MuestraPlantilla;
import com.labflow.model.MuestraPlantilla.EstadoPlantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repositorio para gestionar las relaciones entre Muestras y Plantillas
 */
@Repository
public interface MuestraPlantillaRepository extends JpaRepository<MuestraPlantilla, UUID> {

    /**
     * Buscar todas las plantillas asociadas a una muestra específica
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findByMuestraId(@Param("idMuestra") UUID idMuestra);

    /**
     * Buscar todas las muestras asociadas a una plantilla específica
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.plantilla.idPlantilla = :idPlantilla ORDER BY mp.fechaAgregada DESC")
    List<MuestraPlantilla> findByPlantillaId(@Param("idPlantilla") UUID idPlantilla);

    /**
     * Buscar plantillas de una muestra por estado
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.estadoPlantilla = :estado ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findByMuestraIdAndEstado(@Param("idMuestra") UUID idMuestra, @Param("estado") EstadoPlantilla estado);

    /**
     * Buscar plantillas pendientes para una muestra
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.estadoPlantilla = 'PENDIENTE' ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findPlantillasPendientesByMuestraId(@Param("idMuestra") UUID idMuestra);

    /**
     * Buscar plantillas en proceso para una muestra
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.estadoPlantilla = 'EN_PROCESO' ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findPlantillasEnProcesoByMuestraId(@Param("idMuestra") UUID idMuestra);

    /**
     * Buscar plantillas completadas para una muestra
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.estadoPlantilla = 'COMPLETADA' ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findPlantillasCompletadasByMuestraId(@Param("idMuestra") UUID idMuestra);

    /**
     * Contar plantillas por estado para una muestra
     */
    @Query("SELECT COUNT(mp) FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.estadoPlantilla = :estado")
    Long countByMuestraIdAndEstado(@Param("idMuestra") UUID idMuestra, @Param("estado") EstadoPlantilla estado);

    /**
     * Buscar plantillas por técnico responsable
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.tecnicoResponsable = :tecnico AND mp.estadoPlantilla IN ('PENDIENTE', 'EN_PROCESO') ORDER BY mp.fechaAgregada ASC")
    List<MuestraPlantilla> findByTecnicoResponsable(@Param("tecnico") String tecnico);

    /**
     * Buscar plantillas en rango de fechas
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.fechaAgregada BETWEEN :fechaInicio AND :fechaFin ORDER BY mp.fechaAgregada DESC")
    List<MuestraPlantilla> findByFechaRange(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);

    /**
     * Buscar siguiente orden disponible para una muestra
     */
    @Query("SELECT COALESCE(MAX(mp.ordenEjecucion), 0) + 1 FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra")
    Integer findNextOrdenEjecucion(@Param("idMuestra") UUID idMuestra);

    /**
     * Verificar si existe una plantilla específica para una muestra
     */
    @Query("SELECT COUNT(mp) > 0 FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra AND mp.plantilla.idPlantilla = :idPlantilla")
    boolean existsByMuestraIdAndPlantillaId(@Param("idMuestra") UUID idMuestra, @Param("idPlantilla") UUID idPlantilla);

    /**
     * Buscar plantillas con duración específica
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.fechaInicio IS NOT NULL AND mp.fechaFinalizacion IS NOT NULL ORDER BY mp.fechaFinalizacion DESC")
    List<MuestraPlantilla> findCompletedWithDuration();

    /**
     * Buscar plantillas vencidas (en proceso por más de X horas)
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.estadoPlantilla = 'EN_PROCESO' AND mp.fechaInicio < :fechaLimite")
    List<MuestraPlantilla> findPlantillasVencidas(@Param("fechaLimite") LocalDateTime fechaLimite);

    /**
     * Estadísticas por estado de plantillas
     */
    @Query("SELECT mp.estadoPlantilla, COUNT(mp) FROM MuestraPlantilla mp GROUP BY mp.estadoPlantilla")
    List<Object[]> getEstadisticasByEstado();

    /**
     * Estadísticas por plantilla más usada
     */
    @Query("SELECT p.nombrePlantilla, COUNT(mp) FROM MuestraPlantilla mp JOIN mp.plantilla p GROUP BY p.nombrePlantilla ORDER BY COUNT(mp) DESC")
    List<Object[]> getEstadisticasByPlantilla();

    /**
     * Buscar plantillas por número interno de muestra
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.muestra.numeroInterno = :numeroInterno ORDER BY mp.ordenEjecucion ASC")
    List<MuestraPlantilla> findByNumeroInternoMuestra(@Param("numeroInterno") String numeroInterno);

    /**
     * Buscar plantillas activas (pendientes o en proceso)
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.estadoPlantilla IN ('PENDIENTE', 'EN_PROCESO') ORDER BY mp.fechaAgregada ASC")
    List<MuestraPlantilla> findPlantillasActivas();

    /**
     * Eliminar todas las plantillas de una muestra
     */
    @Query("DELETE FROM MuestraPlantilla mp WHERE mp.muestra.idMuestra = :idMuestra")
    void deleteByMuestraId(@Param("idMuestra") UUID idMuestra);

    /**
     * Buscar plantillas por estado y rango de fechas
     */
    @Query("SELECT mp FROM MuestraPlantilla mp WHERE mp.estadoPlantilla = :estado AND mp.fechaAgregada BETWEEN :fechaInicio AND :fechaFin ORDER BY mp.fechaAgregada DESC")
    List<MuestraPlantilla> findByEstadoAndFechaRange(
        @Param("estado") EstadoPlantilla estado,
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
}