package com.labflow.repository;

import com.labflow.model.Reactivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
    @Query("SELECT r FROM Reactivo r WHERE r.stockActual < r.stockMinimo AND r.activo = true")
    List<Reactivo> findByStockActualLessThanStockMinimoAndActivoTrue();
}
