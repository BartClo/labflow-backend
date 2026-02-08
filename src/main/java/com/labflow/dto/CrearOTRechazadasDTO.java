package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

/**
 * DTO for creating a new Orden de Trabajo with rejected samples from an existing OT.
 * Optionally allows specifying a different technician for the new OT.
 */
public class CrearOTRechazadasDTO {

    @JsonProperty("tecnico_asignado_id")
    private UUID tecnicoAsignadoId;

    @JsonProperty("notas")
    private String notas;

    // Constructors
    public CrearOTRechazadasDTO() {
    }

    // Getters and Setters
    public UUID getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public void setTecnicoAsignadoId(UUID tecnicoAsignadoId) {
        this.tecnicoAsignadoId = tecnicoAsignadoId;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }
}
