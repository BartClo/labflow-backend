package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO para configuración de control de calidad
 */
@Schema(description = "Configuración de control de calidad para un parámetro")
public class ConfigControlCalidadDTO {

    @Schema(description = "ID único de la configuración", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Tipo de control de calidad", 
            example = "Precision/Duplicado",
            allowableValues = {"Precision/Duplicado", "Exactitud/Control estándar", "Fortificada/Spike", "Blanco"})
    private String tipoControl;

    @Schema(description = "Porcentaje mínimo de recuperación aceptable", example = "90.00")
    private BigDecimal recuperacionMin;

    @Schema(description = "Porcentaje máximo de recuperación aceptable", example = "110.00")
    private BigDecimal recuperacionMax;

    // Constructor por defecto
    public ConfigControlCalidadDTO() {
    }

    // Constructor con todos los campos
    public ConfigControlCalidadDTO(UUID id, String tipoControl, BigDecimal recuperacionMin, BigDecimal recuperacionMax) {
        this.id = id;
        this.tipoControl = tipoControl;
        this.recuperacionMin = recuperacionMin;
        this.recuperacionMax = recuperacionMax;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTipoControl() {
        return tipoControl;
    }

    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    public BigDecimal getRecuperacionMin() {
        return recuperacionMin;
    }

    public void setRecuperacionMin(BigDecimal recuperacionMin) {
        this.recuperacionMin = recuperacionMin;
    }

    public BigDecimal getRecuperacionMax() {
        return recuperacionMax;
    }

    public void setRecuperacionMax(BigDecimal recuperacionMax) {
        this.recuperacionMax = recuperacionMax;
    }
}
