package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;

/**
 * DTO for completing a workflow stage.
 * Contains optional notes about the stage completion.
 */
public class CompletarEtapaDTO {

    @JsonProperty("notas")
    @Size(max = 5000, message = "Las notas no pueden exceder 5000 caracteres")
    private String notas;

    // Constructors
    public CompletarEtapaDTO() {
    }

    public CompletarEtapaDTO(String notas) {
        this.notas = notas;
    }

    // Getters and Setters
    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
