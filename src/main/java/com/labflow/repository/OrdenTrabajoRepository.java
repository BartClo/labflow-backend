package com.labflow.repository;

import com.labflow.model.EstadoOT;
import com.labflow.model.OrdenTrabajo;
import com.labflow.model.Usuario;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para gestionar Órdenes de Trabajo
 */
@Repository
public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo, UUID> {

    /**
     * Encuentra todas las órdenes con un estado específico
     */
    List<OrdenTrabajo> findByEstado(EstadoOT estado);

    /**
     * Encuentra todas las órdenes asignadas a un técnico específico
     */
    List<OrdenTrabajo> findByTecnicoAsignado(Usuario tecnico);

    /**
     * Encuentra órdenes por técnico y estado
     */
    List<OrdenTrabajo> findByTecnicoAsignadoAndEstado(Usuario tecnico, EstadoOT estado);

    /**
     * Encuentra órdenes por ID del técnico asignado
     */
    @Query("SELECT ot FROM OrdenTrabajo ot WHERE ot.tecnicoAsignado.id = :tecnicoId")
    List<OrdenTrabajo> findByTecnicoAsignadoId(@Param("tecnicoId") UUID tecnicoId);

    /**
     * Encuentra órdenes por ID del técnico y estado
     */
    @Query("SELECT ot FROM OrdenTrabajo ot WHERE ot.tecnicoAsignado.id = :tecnicoId AND ot.estado = :estado")
    List<OrdenTrabajo> findByTecnicoAsignadoIdAndEstado(@Param("tecnicoId") UUID tecnicoId, @Param("estado") EstadoOT estado);

    /**
     * Obtiene todas las órdenes ordenadas por fecha de creación descendente
     */
    List<OrdenTrabajo> findAllByOrderByFechaCreacionDesc();

    /**
     * Encuentra la última orden de trabajo cuyo código empiece con el prefijo dado
     * Usado para generación automática de código OT secuencial
     */
    @Query("SELECT ot FROM OrdenTrabajo ot " +
           "WHERE ot.codigoOT LIKE CONCAT(:prefix, '%') " +
           "ORDER BY ot.codigoOT DESC")
    List<OrdenTrabajo> findFirstByCodigoOTStartingWithOrderByCodigoOTDesc(@Param("prefix") String prefix, Pageable pageable);
}
