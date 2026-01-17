package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * DTO básico para mostrar información resumida de una tarea (MuestraAnalisis)
 */
public class TareaBasicDTO {

    @JsonProperty("id_muestra_analisis")
    private UUID idMuestraAnalisis;

    @JsonProperty("numero_muestra")
    private String numeroMuestra;

    @JsonProperty("nombre_analisis")
    private String nombreAnalisis;

    @JsonProperty("estado_analisis")
    private String estadoAnalisis;

    @JsonProperty("cumple_normativa")
    private Boolean cumpleNormativa;

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

    public Boolean getCumpleNormativa() {
        return cumpleNormativa;
    }

    public void setCumpleNormativa(Boolean cumpleNormativa) {
        this.cumpleNormativa = cumpleNormativa;
    }
}
