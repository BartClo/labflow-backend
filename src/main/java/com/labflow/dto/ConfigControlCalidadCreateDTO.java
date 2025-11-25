package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO para crear una nueva configuración de control de calidad
 */
@Schema(description = "Datos para crear una configuración de control de calidad")
public class ConfigControlCalidadCreateDTO {

    @NotBlank(message = "El tipo de control es obligatorio")
    @Size(max = 100, message = "El tipo de control no puede exceder 100 caracteres")
    @Pattern(regexp = "^(Precision/Duplicado|Exactitud/Control estándar|Fortificada/Spike|Blanco)$",
             message = "El tipo de control debe ser uno de: Precision/Duplicado, Exactitud/Control estándar, Fortificada/Spike, Blanco")
    @Schema(description = "Tipo de control de calidad", 
            example = "Precision/Duplicado",
            allowableValues = {"Precision/Duplicado", "Exactitud/Control estándar", "Fortificada/Spike", "Blanco"})
    private String tipoControl;

    @NotNull(message = "El porcentaje mínimo de recuperación es obligatorio")
    @DecimalMin(value = "0.0", message = "El porcentaje mínimo debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje mínimo no puede exceder 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje debe tener máximo 3 enteros y 2 decimales")
    @Schema(description = "Porcentaje mínimo de recuperación aceptable", example = "90.00")
    private BigDecimal recuperacionMin;

    @NotNull(message = "El porcentaje máximo de recuperación es obligatorio")
    @DecimalMin(value = "0.0", message = "El porcentaje máximo debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje máximo no puede exceder 100")
    @Digits(integer = 3, fraction = 2, message = "El porcentaje debe tener máximo 3 enteros y 2 decimales")
    @Schema(description = "Porcentaje máximo de recuperación aceptable", example = "110.00")
    private BigDecimal recuperacionMax;

    // Constructor por defecto
    public ConfigControlCalidadCreateDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Constructor con todos los campos
    public ConfigControlCalidadCreateDTO(String tipoControl, BigDecimal recuperacionMin, BigDecimal recuperacionMax) {
        this.tipoControl = tipoControl;
        this.recuperacionMin = recuperacionMin;
        this.recuperacionMax = recuperacionMax;
    }

    // Getters y Setters
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
