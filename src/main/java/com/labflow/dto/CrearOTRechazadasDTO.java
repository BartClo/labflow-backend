package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;

/**
 * DTO for creating a new Orden de Trabajo with rejected samples from an existing OT.
 * Optionally allows specifying a different technician for the new OT.
 * If tarea_ids is provided, those tasks will be marked as rejected (cumpleNormativa=false)
 * before creating the new OT.
 */
public class CrearOTRechazadasDTO {

    @JsonProperty("tecnico_asignado_id")
    private UUID tecnicoAsignadoId;

    @JsonProperty("notas")
    private String notas;

    @JsonProperty("tarea_ids")
    private List<UUID> tareaIds;

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

    public List<UUID> getTareaIds() {
        return tareaIds;
    }

    public void setTareaIds(List<UUID> tareaIds) {
        this.tareaIds = tareaIds;
    }
}
