package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * DTO básico para mostrar información resumida de una tarea (MuestraAnalisis)
 */
public class TareaBasicDTO {

    @JsonProperty("id_muestra_analisis")
    private UUID idMuestraAnalisis;

    @JsonProperty("id_muestra")
    private UUID idMuestra;

    @JsonProperty("numero_muestra")
    private String numeroMuestra;

    @JsonProperty("nombre_analisis")
    private String nombreAnalisis;

    @JsonProperty("estado_analisis")
    private String estadoAnalisis;

    @JsonProperty("codigo_barras")
    private String codigoBarras;

    @JsonProperty("cumple_normativa")
    private Boolean cumpleNormativa;

    @JsonProperty("unidad_medida")
    private String unidadMedida;

    @JsonProperty("limite_minimo")
    private String limiteMinimo;

    @JsonProperty("limite_maximo")
    private String limiteMaximo;

    @JsonProperty("nombre_parametro")
    private String nombreParametro;

    @JsonProperty("prioridad")
    private String prioridad;

    // Constructores
    public TareaBasicDTO() {
    }

    // Getters and Setters
    public UUID getIdMuestraAnalisis() {
        return idMuestraAnalisis;
    }

    public void setIdMuestraAnalisis(UUID idMuestraAnalisis) {
        this.idMuestraAnalisis = idMuestraAnalisis;
    }

    public UUID getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(UUID idMuestra) {
        this.idMuestra = idMuestra;
    }

    public String getNumeroMuestra() {
        return numeroMuestra;
    }

    public void setNumeroMuestra(String numeroMuestra) {
        this.numeroMuestra = numeroMuestra;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }

    public String getEstadoAnalisis() {
        return estadoAnalisis;
    }

    public void setEstadoAnalisis(String estadoAnalisis) {
        this.estadoAnalisis = estadoAnalisis;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public Boolean getCumpleNormativa() {
        return cumpleNormativa;
    }

    public void setCumpleNormativa(Boolean cumpleNormativa) {
        this.cumpleNormativa = cumpleNormativa;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getLimiteMinimo() {
        return limiteMinimo;
    }

    public void setLimiteMinimo(String limiteMinimo) {
        this.limiteMinimo = limiteMinimo;
    }

    public String getLimiteMaximo() {
        return limiteMaximo;
    }

    public void setLimiteMaximo(String limiteMaximo) {
        this.limiteMaximo = limiteMaximo;
    }

    public String getNombreParametro() {
        return nombreParametro;
    }

    public void setNombreParametro(String nombreParametro) {
        this.nombreParametro = nombreParametro;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}
