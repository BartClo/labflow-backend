package com.labflow.repository;

import com.labflow.model.ConfigControlCalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repositorio para gestionar las configuraciones de control de calidad
 */
@Repository
public interface ConfigControlCalidadRepository extends JpaRepository<ConfigControlCalidad, UUID> {

    /**
     * Busca todas las configuraciones de un parámetro específico
     */
    @Query("SELECT c FROM ConfigControlCalidad c WHERE c.parametro.id = :parametroId")
    List<ConfigControlCalidad> findByParametroId(@Param("parametroId") UUID parametroId);

    /**
     * Busca configuraciones por tipo de control
     */
    @Query("SELECT c FROM ConfigControlCalidad c WHERE c.tipoControl = :tipoControl")
    List<ConfigControlCalidad> findByTipoControl(@Param("tipoControl") String tipoControl);

    /**
     * Busca una configuración específica de un parámetro por tipo de control
     */
    @Query("SELECT c FROM ConfigControlCalidad c WHERE c.parametro.id = :parametroId AND c.tipoControl = :tipoControl")
    ConfigControlCalidad findByParametroIdAndTipoControl(@Param("parametroId") UUID parametroId, @Param("tipoControl") String tipoControl);
}
