package com.labflow.repository;

import com.labflow.model.OrdenTrabajo;
import com.labflow.model.OrdenTrabajoEtapa;
import com.labflow.model.TipoEtapaWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for managing OrdenTrabajoEtapa entities.
 * Provides access to workflow stages associated with work orders.
 */
@Repository
public interface OrdenTrabajoEtapaRepository extends JpaRepository<OrdenTrabajoEtapa, UUID> {

    /**
     * Finds all stages for a given OrdenTrabajo, ordered by execution sequence.
     * @param ordenTrabajo The work order
     * @return List of stages ordered by sequence (1-4)
     */
    List<OrdenTrabajoEtapa> findByOrdenTrabajoOrderByOrdenSecuenciaAsc(OrdenTrabajo ordenTrabajo);

    /**
     * Finds a specific stage type for a given OrdenTrabajo.
     * @param ordenTrabajo The work order
     * @param tipoEtapa The stage type to find
     * @return Optional containing the stage if found
     */
    Optional<OrdenTrabajoEtapa> findByOrdenTrabajoAndTipoEtapa(OrdenTrabajo ordenTrabajo, TipoEtapaWorkflow tipoEtapa);

    /**
     * Finds all stages for a given OrdenTrabajo by its ID.
     * @param ordenTrabajoId The work order ID
     * @return List of stages ordered by sequence
     */
    List<OrdenTrabajoEtapa> findByOrdenTrabajo_IdOrderByOrdenSecuenciaAsc(UUID ordenTrabajoId);

    /**
     * Deletes all stages associated with a specific OrdenTrabajo.
     * @param ordenTrabajo The work order
     */
    void deleteByOrdenTrabajo(OrdenTrabajo ordenTrabajo);
}
