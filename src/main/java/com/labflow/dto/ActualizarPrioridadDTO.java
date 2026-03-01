package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ActualizarPrioridadDTO {

    @NotBlank(message = "La prioridad es requerida")
    @Pattern(regexp = "(?i)(alta|media|baja)", message = "La prioridad debe ser alta, media o baja")
    @JsonProperty("prioridad")
    private String prioridad;

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }
}