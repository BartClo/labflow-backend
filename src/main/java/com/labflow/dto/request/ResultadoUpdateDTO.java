package com.labflow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * DTO para actualizar los resultados de un análisis
 */
public class ResultadoUpdateDTO {

    @NotNull(message = "Los resultados de los parámetros son requeridos")
    @JsonProperty("parametros")
    private Map<String, ParametroResultadoDTO> parametros;

    @JsonProperty("controles_calidad")
    private Map<String, Object> controlesCalidad;

    @JsonProperty("metadata")
    private Map<String, Object> metadata;

    // Constructores
    public ResultadoUpdateDTO() {
    }

    // Getters and Setters
    public Map<String, ParametroResultadoDTO> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, ParametroResultadoDTO> parametros) {
        this.parametros = parametros;
    }

    public Map<String, Object> getControlesCalidad() {
        return controlesCalidad;
    }

    public void setControlesCalidad(Map<String, Object> controlesCalidad) {
        this.controlesCalidad = controlesCalidad;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    /**
     * DTO anidado para resultado de un parámetro individual
     */
    public static class ParametroResultadoDTO {
        @JsonProperty("valor")
        private String valor;

        @JsonProperty("unidad")
        private String unidad;

        public ParametroResultadoDTO() {
        }

        public ParametroResultadoDTO(String valor, String unidad) {
            this.valor = valor;
            this.unidad = unidad;
        }

        public String getValor() {
            return valor;
        }

        public void setValor(String valor) {
            this.valor = valor;
        }

        public String getUnidad() {
            return unidad;
        }

        public void setUnidad(String unidad) {
            this.unidad = unidad;
        }
    }
}
