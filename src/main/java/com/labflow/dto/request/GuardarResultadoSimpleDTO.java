package com.labflow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO simplificado para guardar un resultado desde el modal de captura.
 * Se usa cuando el análisis tiene un solo parámetro principal y el operador
 * ingresa un único valor medido.
 */
public class GuardarResultadoSimpleDTO {

    @NotNull(message = "El ID de muestra-análisis es requerido")
    @JsonProperty("id_muestra_analisis")
    private UUID idMuestraAnalisis;

    @JsonProperty("codigo_barras")
    private String codigoBarras;

    @NotNull(message = "El valor medido es requerido")
    @JsonProperty("valor_medido")
    private String valorMedido;

    @JsonProperty("observaciones")
    private String observaciones;

    // Constructores
    public GuardarResultadoSimpleDTO() {
    }

    // Getters y Setters
    public UUID getIdMuestraAnalisis() {
        return idMuestraAnalisis;
    }

    public void setIdMuestraAnalisis(UUID idMuestraAnalisis) {
        this.idMuestraAnalisis = idMuestraAnalisis;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getValorMedido() {
        return valorMedido;
    }

    public void setValorMedido(String valorMedido) {
        this.valorMedido = valorMedido;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
