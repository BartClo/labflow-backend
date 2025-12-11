package com.labflow.repository;

import com.labflow.model.Reactivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad Reactivo
 */
@Repository
public interface ReactivoRepository extends JpaRepository<Reactivo, UUID> {

    /**
     * Buscar reactivo por código
     */
    Optional<Reactivo> findByCodigo(String codigo);

    /**
     * Listar reactivos activos
     */
    List<Reactivo> findByActivoTrue();

    /**
     * Buscar reactivos por vencimiento cercano
     */
    List<Reactivo> findByFechaVencimientoBeforeAndActivoTrue(LocalDate fecha);

    /**
     * Buscar reactivos por stock bajo
     */
    List<Reactivo> findByStockActualLessThanStockMinimoAndActivoTrue();
}
