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

    @JsonProperty("valor")
    @Size(max = 5000, message = "El valor no puede exceder 5000 caracteres")
    private String valor;

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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
