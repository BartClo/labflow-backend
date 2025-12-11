package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

/**
 * DTO para respuesta de parámetro con sus configuraciones de control de calidad
 */
@Schema(description = "Parámetro de medición de un análisis con sus configuraciones de control de calidad")
public class ParametroDTO {

    @Schema(description = "ID único del parámetro", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "ID del análisis al que pertenece", example = "550e8400-e29b-41d4-a716-446655440001")
    private UUID idAnalisis;

    @Schema(description = "Nombre del parámetro", example = "pH")
    private String nombre;

    @Schema(description = "Unidad de medida", example = "Unidades de pH")
    private String unidad;

    @Schema(description = "Valor máximo permitido según normativa (puede ser numérico o texto como 'Ausencia', '<LDM')", example = "8.5")
    private String valorMaximoNormativa;

    @Schema(description = "Lista de configuraciones de control de calidad")
    private List<ConfigControlCalidadDTO> configuracionesControl;

    // Constructor por defecto
    public ParametroDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public List<ConfigControlCalidadDTO> getConfiguracionesControl() {
        return configuracionesControl;
    }

    public void setConfiguracionesControl(List<ConfigControlCalidadDTO> configuracionesControl) {
        this.configuracionesControl = configuracionesControl;
    }
}
