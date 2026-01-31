package com.labflow.repository;

import com.labflow.model.MuestraAnalisis;
import com.labflow.model.OrdenTrabajo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad MuestraAnalisis
 * Maneja las relaciones entre muestras y análisis
 */
@Repository
public interface MuestraAnalisisRepository extends JpaRepository<MuestraAnalisis, UUID> {

    // Búsquedas por muestra
    List<MuestraAnalisis> findByMuestraIdMuestra(UUID idMuestra);
    
    List<MuestraAnalisis> findByMuestraIdMuestraOrderByOrdenEjecucionAsc(UUID idMuestra);
    
    Page<MuestraAnalisis> findByMuestraIdMuestra(UUID idMuestra, Pageable pageable);

    // Búsquedas por análisis
    List<MuestraAnalisis> findByAnalisisIdAnalisis(UUID idAnalisis);
    
    Page<MuestraAnalisis> findByAnalisisIdAnalisis(UUID idAnalisis, Pageable pageable);

    // Búsquedas por estado
    List<MuestraAnalisis> findByEstadoAnalisis(MuestraAnalisis.EstadoAnalisis estado);
    
    Page<MuestraAnalisis> findByEstadoAnalisis(MuestraAnalisis.EstadoAnalisis estado, Pageable pageable);

    // Búsquedas específicas para una muestra y análisis
    Optional<MuestraAnalisis> findByMuestraIdMuestraAndAnalisisIdAnalisis(UUID idMuestra, UUID idAnalisis);
    
    boolean existsByMuestraIdMuestraAndAnalisisIdAnalisis(UUID idMuestra, UUID idAnalisis);

    // Búsquedas por técnico responsable
    List<MuestraAnalisis> findByTecnicoResponsable(String tecnicoResponsable);
    
    List<MuestraAnalisis> findByTecnicoResponsableAndEstadoAnalisis(
            String tecnicoResponsable, 
            MuestraAnalisis.EstadoAnalisis estado);

    // Búsquedas por fechas
    List<MuestraAnalisis> findByFechaInicioBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<MuestraAnalisis> findByFechaFinalizacionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Consultas complejas para gestión del laboratorio
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'PENDIENTE' " +
           "ORDER BY ma.muestra.prioridad DESC, ma.ordenEjecucion ASC, ma.fechaAgregado ASC")
    List<MuestraAnalisis> findAnalisisPendientesOrdenados();

    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'EN_PROCESO' " +
           "ORDER BY ma.fechaInicio ASC")
    List<MuestraAnalisis> findAnalisisEnProceso();

    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'COMPLETADO' " +
           "AND ma.fechaFinalizacion BETWEEN :inicio AND :fin " +
           "ORDER BY ma.fechaFinalizacion DESC")
    List<MuestraAnalisis> findAnalisisCompletadosEnPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Estadísticas por muestra
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma WHERE ma.muestra.idMuestra = :idMuestra")
    Long countByMuestraId(@Param("idMuestra") UUID idMuestra);

    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.idMuestra = :idMuestra AND ma.estadoAnalisis = :estado")
    Long countByMuestraIdAndEstado(
            @Param("idMuestra") UUID idMuestra,
            @Param("estado") MuestraAnalisis.EstadoAnalisis estado);

    // Estadísticas por análisis
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma WHERE ma.analisis.idAnalisis = :idAnalisis")
    Long countByAnalisisId(@Param("idAnalisis") UUID idAnalisis);

    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.analisis.idAnalisis = :idAnalisis AND ma.estadoAnalisis = :estado")
    Long countByAnalisisIdAndEstado(
            @Param("idAnalisis") UUID idAnalisis,
            @Param("estado") MuestraAnalisis.EstadoAnalisis estado);

    @Query("SELECT a.nombreAnalisis, COUNT(ma) " +
           "FROM MuestraAnalisis ma " +
           "JOIN ma.analisis a " +
           "WHERE ma.estadoAnalisis = 'COMPLETADO' " +
           "AND ma.fechaFinalizacion BETWEEN :inicio AND :fin " +
           "GROUP BY a.nombreAnalisis " +
           "ORDER BY COUNT(ma) DESC")
    List<Object[]> getAnalisisMasRealizados(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    @Query("SELECT COUNT(ma) " +
           "FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'COMPLETADO' " +
           "AND ma.analisis.idAnalisis = :idAnalisis " +
           "AND ma.fechaFinalizacion IS NOT NULL " +
           "AND ma.fechaInicio IS NOT NULL")
    Long getTiempoPromedioAnalisisEnHoras(@Param("idAnalisis") UUID idAnalisis);

    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'COMPLETADO' " +
           "AND ma.fechaFinalizacion IS NOT NULL " +
           "AND ma.fechaInicio IS NOT NULL")
    List<MuestraAnalisis> findAnalisisQueSuperanTiempoLimite();

    // Consultas para control de calidad
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'EN_PROCESO' " +
           "AND ma.fechaInicio < :fechaLimite")
    List<MuestraAnalisis> findAnalisisEnProcesoRetrasados(@Param("fechaLimite") LocalDateTime fechaLimite);

    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'PENDIENTE' " +
           "AND ma.muestra.prioridad = 'ALTA' " +
           "ORDER BY ma.fechaAgregado ASC")
    List<MuestraAnalisis> findAnalisisPendientesAltaPrioridad();

    // Consultas para asignación de trabajo
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'PENDIENTE' " +
           "AND ma.analisis.idAnalisis = :idAnalisis " +
           "ORDER BY ma.muestra.prioridad DESC, ma.ordenEjecucion ASC")
    List<MuestraAnalisis> findAnalisisPendientesPorTipo(@Param("idAnalisis") UUID idAnalisis);

    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.tecnicoResponsable = :tecnico " +
           "AND ma.estadoAnalisis = 'EN_PROCESO' " +
           "ORDER BY ma.fechaInicio ASC")
    List<MuestraAnalisis> findAnalisisEnProcesoByTecnico(@Param("tecnico") String tecnico);

    // Validaciones de integridad
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.idMuestra = :idMuestra " +
           "AND ma.ordenEjecucion = :orden")
    Long countByMuestraIdAndOrdenEjecucion(
            @Param("idMuestra") UUID idMuestra,
            @Param("orden") Integer orden);

    // Consulta para obtener el siguiente número de orden para una muestra
    @Query("SELECT COALESCE(MAX(ma.ordenEjecucion), 0) + 1 " +
           "FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.idMuestra = :idMuestra")
    Integer getSiguienteOrdenEjecucion(@Param("idMuestra") UUID idMuestra);

    // Análisis por cliente (a través de la muestra)
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.cliente.idCliente = :idCliente " +
           "ORDER BY ma.fechaAgregado DESC")
    List<MuestraAnalisis> findAnalisisByClienteId(@Param("idCliente") UUID idCliente);

    // Dashboard de trabajo diario
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.tecnicoResponsable = :tecnico " +
           "AND ma.estadoAnalisis IN ('PENDIENTE', 'EN_PROCESO') " +
           "ORDER BY ma.muestra.prioridad DESC, ma.ordenEjecucion ASC")
    List<MuestraAnalisis> findTrabajoDelDiaByTecnico(@Param("tecnico") String tecnico);

    // Eliminar análisis de una muestra específica
    void deleteByMuestraIdMuestra(UUID idMuestra);
    
    void deleteByMuestraIdMuestraAndAnalisisIdAnalisis(UUID idMuestra, UUID idAnalisis);

    // ============================================================================
    // Consultas para Órdenes de Trabajo
    // ============================================================================

    /**
     * Encuentra tareas pendientes sin orden de trabajo asignada
     */
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'PENDIENTE' " +
           "AND ma.ordenTrabajo IS NULL " +
           "ORDER BY ma.muestra.prioridad DESC, ma.fechaAgregado ASC")
    List<MuestraAnalisis> findTareasPendientesSinOrden();

    /**
     * Encuentra tareas pendientes sin orden de trabajo para un análisis específico
     */
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.estadoAnalisis = 'PENDIENTE' " +
           "AND ma.ordenTrabajo IS NULL " +
           "AND ma.analisis.idAnalisis = :analisisId " +
           "ORDER BY ma.muestra.prioridad DESC, ma.fechaAgregado ASC")
    List<MuestraAnalisis> findTareasPendientesSinOrdenByAnalisis(@Param("analisisId") UUID analisisId);

    /**
     * Encuentra todas las tareas de una orden de trabajo
     */
    List<MuestraAnalisis> findByOrdenTrabajo(OrdenTrabajo ordenTrabajo);

    /**
     * Encuentra tareas por ID de orden de trabajo
     */
    @Query("SELECT ma FROM MuestraAnalisis ma WHERE ma.ordenTrabajo.id = :ordenTrabajoId")
    List<MuestraAnalisis> findByOrdenTrabajoId(@Param("ordenTrabajoId") UUID ordenTrabajoId);

    /**
     * Cuenta tareas pendientes en una orden de trabajo
     */
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.ordenTrabajo.id = :ordenTrabajoId " +
           "AND ma.estadoAnalisis = 'PENDIENTE'")
    Long countTareasPendientesByOrdenTrabajo(@Param("ordenTrabajoId") UUID ordenTrabajoId);

    /**
     * Cuenta tareas por orden de trabajo y estado
     */
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.ordenTrabajo = :ordenTrabajo " +
           "AND ma.estadoAnalisis = :estado")
    Long countByOrdenTrabajoAndEstadoAnalisis(
            @Param("ordenTrabajo") OrdenTrabajo ordenTrabajo,
            @Param("estado") MuestraAnalisis.EstadoAnalisis estado);

    // ============================================================================
    // Consultas para Workflow Pull
    // ============================================================================

    /**
     * Busca tarea por código de barras de muestra y estado EN_PROCESO
     * Usado para búsqueda por QR en el workflow
     */
    @Query("SELECT ma FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.codigoBarras = :codigoBarras " +
           "AND ma.estadoAnalisis = 'EN_PROCESO' " +
           "ORDER BY ma.fechaAgregado ASC")
    List<MuestraAnalisis> findByMuestra_CodigoBarrasAndEstado(@Param("codigoBarras") String codigoBarras);

    /**
     * Agrupa tareas pendientes sin OT por nombre de análisis
     * Retorna un mapa con el nombre del análisis y el conteo
     */
    @Query("SELECT ma.analisis.nombreAnalisis as nombreAnalisis, COUNT(ma) as cantidad " +
           "FROM MuestraAnalisis ma " +
           "WHERE ma.ordenTrabajo IS NULL " +
           "AND ma.estadoAnalisis = 'PENDIENTE' " +
           "GROUP BY ma.analisis.nombreAnalisis " +
           "ORDER BY COUNT(ma) DESC")
    List<Object[]> countTareasPendientesPorAnalisis();

    /**
     * Libera tareas de una orden de trabajo cancelada
     * Solo libera tareas EN_PROCESO sin resultado
     */
    @Modifying
    @Query("UPDATE MuestraAnalisis ma " +
           "SET ma.ordenTrabajo = NULL, " +
           "    ma.estadoAnalisis = 'PENDIENTE', " +
           "    ma.fechaInicio = NULL, " +
           "    ma.tecnicoResponsable = NULL " +
           "WHERE ma.ordenTrabajo.id = :ordenTrabajoId " +
           "AND ma.estadoAnalisis = 'EN_PROCESO' " +
           "AND ma.resultado IS NULL")
    int liberarTareasDeOrdenCancelada(@Param("ordenTrabajoId") UUID ordenTrabajoId);

    /**
     * Cuenta tareas no finalizadas en una orden de trabajo
     * Usado para validar generación de informes
     */
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.ordenTrabajo.id = :ordenTrabajoId " +
           "AND ma.estadoAnalisis NOT IN ('COMPLETADO', 'VALIDADO')")
    Long countTareasNoFinalizadasByOrdenTrabajo(@Param("ordenTrabajoId") UUID ordenTrabajoId);

    /**
     * Cuenta tareas con resultados (COMPLETADO o VALIDADO) por muestra
     * Usado para validar eliminación de muestras
     */
    @Query("SELECT COUNT(ma) FROM MuestraAnalisis ma " +
           "WHERE ma.muestra.idMuestra = :idMuestra " +
           "AND ma.estadoAnalisis IN ('COMPLETADO', 'VALIDADO')")
    Long countTareasConResultadosByMuestra(@Param("idMuestra") UUID idMuestra);
}