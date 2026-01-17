package com.labflow.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * DTO para crear una nueva Orden de Trabajo
 */
public class OrdenTrabajoCreateDTO {

    @NotEmpty(message = "Debe proporcionar al menos una tarea")
    @JsonProperty("tarea_ids")
    private List<UUID> tareaIds;

    @NotNull(message = "Debe asignar un técnico a la orden de trabajo")
    @JsonProperty("tecnico_asignado_id")
    private UUID tecnicoAsignadoId;

    // Constructores
    public OrdenTrabajoCreateDTO() {
    }

    public OrdenTrabajoCreateDTO(List<UUID> tareaIds, UUID tecnicoAsignadoId) {
        this.tareaIds = tareaIds;
        this.tecnicoAsignadoId = tecnicoAsignadoId;
    }

    // Getters and Setters
    public List<UUID> getTareaIds() {
        return tareaIds;
    }

    public void setTareaIds(List<UUID> tareaIds) {
        this.tareaIds = tareaIds;
    }

    public UUID getTecnicoAsignadoId() {
        return tecnicoAsignadoId;
    }

    public void setTecnicoAsignadoId(UUID tecnicoAsignadoId) {
        this.tecnicoAsignadoId = tecnicoAsignadoId;
    }
}
