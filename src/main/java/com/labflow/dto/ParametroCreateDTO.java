package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO para crear un nuevo parámetro con sus configuraciones de control de calidad
 */
@Schema(description = "Datos para crear un nuevo parámetro de medición")
public class ParametroCreateDTO {

    @NotNull(message = "El ID del análisis es obligatorio")
    @Schema(description = "ID del análisis al que pertenece el parámetro", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID idAnalisis;

    @NotBlank(message = "El nombre del parámetro es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre debe tener entre 1 y 255 caracteres")
    @Schema(description = "Nombre del parámetro", example = "pH")
    private String nombre;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(min = 1, max = 100, message = "La unidad debe tener entre 1 y 100 caracteres")
    @Schema(description = "Unidad de medida", example = "Unidades de pH")
    private String unidad;

    @Size(max = 255, message = "El valor máximo normativa no puede exceder 255 caracteres")
    @Schema(description = "Valor máximo permitido según normativa (puede ser numérico o texto como 'Ausencia', '<LDM')", example = "8.5")
    private String valorMaximoNormativa;

    @Valid
    @Schema(description = "Lista de configuraciones de control de calidad para el parámetro")
    private List<ConfigControlCalidadCreateDTO> configuracionesControl;

    // Constructor por defecto
    public ParametroCreateDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
    public UUID getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(UUID idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getValorMaximoNormativa() {
        return valorMaximoNormativa;
    }

    public void setValorMaximoNormativa(String valorMaximoNormativa) {
        this.valorMaximoNormativa = valorMaximoNormativa;
    }

    public List<ConfigControlCalidadCreateDTO> getConfiguracionesControl() {
        return configuracionesControl;
    }

    public void setConfiguracionesControl(List<ConfigControlCalidadCreateDTO> configuracionesControl) {
        this.configuracionesControl = configuracionesControl;
    }
}
