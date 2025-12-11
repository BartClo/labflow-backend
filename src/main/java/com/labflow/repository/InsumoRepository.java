package com.labflow.repository;

import com.labflow.model.Insumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio para la entidad Insumo
 */
@Repository
public interface InsumoRepository extends JpaRepository<Insumo, UUID> {

    /**
     * Buscar insumo por código
     */
    Optional<Insumo> findByCodigo(String codigo);

    /**
     * Listar insumos activos
     */
    List<Insumo> findByActivoTrue();

    /**
     * Buscar insumos por categoría
     */
    List<Insumo> findByCategoriaContainingIgnoreCaseAndActivoTrue(String categoria);

    /**
     * Buscar insumos con stock bajo
     */
    List<Insumo> findByStockActualLessThanStockMinimoAndActivoTrue();
}
