package com.labflow.repository;

import com.labflow.model.Muestra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad Muestra
 * Incluye consultas personalizadas para búsquedas avanzadas
 */
@Repository
public interface MuestraRepository extends JpaRepository<Muestra, UUID> {

    // Búsquedas básicas
    Optional<Muestra> findByNumeroInterno(String numeroInterno);
    
    // Buscar última muestra por prefijo de año para generar código correlativo
    Optional<Muestra> findTopByNumeroInternoStartingWithOrderByNumeroInternoDesc(String prefijo);
    
    Optional<Muestra> findByCodigoBarras(String codigoBarras);
    
    List<Muestra> findByClienteIdCliente(UUID idCliente);
    
    List<Muestra> findByEstado(Muestra.EstadoMuestra estado);
    
    List<Muestra> findByPrioridad(Muestra.Prioridad prioridad);

    // Búsquedas con paginación
    Page<Muestra> findByClienteIdCliente(UUID idCliente, Pageable pageable);
    
    Page<Muestra> findByEstado(Muestra.EstadoMuestra estado, Pageable pageable);
    
    Page<Muestra> findByPrioridad(Muestra.Prioridad prioridad, Pageable pageable);

    // Búsquedas por fechas
    List<Muestra> findByFechaMuestreoBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Muestra> findByFechaRecepcionBetween(LocalDateTime inicio, LocalDateTime fin);
    
    List<Muestra> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);

    // Búsquedas con múltiples criterios
    @Query("SELECT m FROM Muestra m WHERE " +
           "(:clienteId IS NULL OR m.cliente.idCliente = :clienteId) AND " +
           "(:estado IS NULL OR m.estado = :estado) AND " +
           "(:prioridad IS NULL OR m.prioridad = :prioridad) AND " +
           "(:fechaInicio IS NULL OR m.fechaMuestreo >= :fechaInicio) AND " +
           "(:fechaFin IS NULL OR m.fechaMuestreo <= :fechaFin)")
    Page<Muestra> findByMultiplesCriterios(
            @Param("clienteId") UUID clienteId,
            @Param("estado") Muestra.EstadoMuestra estado,
            @Param("prioridad") Muestra.Prioridad prioridad,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin,
            Pageable pageable);

    // Búsqueda de texto libre
    @Query("SELECT m FROM Muestra m WHERE " +
           "LOWER(m.numeroInterno) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(m.codigoBarras) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(m.puntoMuestreo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(m.tipoMuestra) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(m.responsableMuestreo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(m.observaciones) LIKE LOWER(CONCAT('%', :texto, '%'))")
    Page<Muestra> buscarPorTexto(@Param("texto") String texto, Pageable pageable);

    // Búsquedas específicas para el laboratorio
    @Query("SELECT m FROM Muestra m WHERE m.estado IN :estados ORDER BY m.prioridad DESC, m.fechaRecepcion ASC")
    List<Muestra> findMuestrasParaProcesar(@Param("estados") List<Muestra.EstadoMuestra> estados);

    @Query("SELECT m FROM Muestra m WHERE m.prioridad = 'ALTA' AND m.estado NOT IN ('COMPLETADA', 'RECHAZADA')")
    List<Muestra> findMuestrasAltaPrioridadPendientes();

    // Estadísticas
    @Query("SELECT COUNT(m) FROM Muestra m WHERE m.estado = :estado")
    Long countByEstado(@Param("estado") Muestra.EstadoMuestra estado);

    @Query("SELECT COUNT(m) FROM Muestra m WHERE m.cliente.idCliente = :clienteId")
    Long countByClienteId(@Param("clienteId") UUID clienteId);

    @Query("SELECT COUNT(m) FROM Muestra m WHERE m.fechaCreacion >= :fecha")
    Long countMuestrasDesde(@Param("fecha") LocalDateTime fecha);

    // Consulta para muestras con análisis pendientes
    @Query("SELECT DISTINCT m FROM Muestra m " +
           "JOIN m.muestraAnalisis ma " +
           "WHERE ma.estadoAnalisis IN ('PENDIENTE', 'EN_PROCESO')")
    List<Muestra> findMuestrasConAnalisisPendientes();

    // Consulta para muestras completadas en un periodo
    @Query("SELECT m FROM Muestra m WHERE " +
           "m.estado = 'COMPLETADA' AND " +
           "m.fechaActualizacion BETWEEN :inicio AND :fin")
    List<Muestra> findMuestrasCompletadasEnPeriodo(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    // Validaciones de unicidad
    boolean existsByNumeroInterno(String numeroInterno);
    
    boolean existsByCodigoBarras(String codigoBarras);
    
    boolean existsByNumeroInternoAndIdMuestraNotIn(String numeroInterno, List<UUID> excludeIds);
    
    boolean existsByCodigoBarrasAndIdMuestraNotIn(String codigoBarras, List<UUID> excludeIds);

    // Consultas para reportes
    @Query("SELECT DATE(m.fechaCreacion) as fecha, COUNT(m) as cantidad " +
           "FROM Muestra m " +
           "WHERE m.fechaCreacion BETWEEN :inicio AND :fin " +
           "GROUP BY DATE(m.fechaCreacion) " +
           "ORDER BY DATE(m.fechaCreacion)")
    List<Object[]> getMuestrasGroupedByDate(
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    @Query("SELECT m.estado, COUNT(m) " +
           "FROM Muestra m " +
           "GROUP BY m.estado")
    List<Object[]> getEstadisticasPorEstado();

    @Query("SELECT c.nombreCliente, COUNT(m) " +
           "FROM Muestra m " +
           "JOIN m.cliente c " +
           "GROUP BY c.nombreCliente " +
           "ORDER BY COUNT(m) DESC")
    List<Object[]> getEstadisticasPorCliente();

    // Consulta para dashboard del laboratorio
    @Query("SELECT m FROM Muestra m " +
           "WHERE m.estado IN ('RECIBIDA', 'EN_PROCESO') " +
           "ORDER BY m.prioridad DESC, m.fechaRecepcion ASC")
    List<Muestra> findMuestrasEnCola(Pageable pageable);

    // Muestras que requieren atención inmediata
    @Query("SELECT m FROM Muestra m WHERE " +
           "(m.prioridad = 'ALTA' AND m.estado = 'RECIBIDA') OR " +
           "(m.fechaRecepcion < :fechaLimite AND m.estado IN ('RECIBIDA', 'EN_PROCESO'))")
    List<Muestra> findMuestrasRequierenAtencion(@Param("fechaLimite") LocalDateTime fechaLimite);
}