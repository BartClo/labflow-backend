package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para representar una Orden de Trabajo
 */
public class OrdenTrabajoDTO {

    @JsonProperty("id_orden_trabajo")
    private UUID idOrdenTrabajo;

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @JsonProperty("fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @JsonProperty("tecnico_asignado")
    private UsuarioBasicDTO tecnicoAsignado;

    @JsonProperty("tareas")
    private List<TareaBasicDTO> tareas;

    @JsonProperty("total_tareas")
    private Integer totalTareas;

    @JsonProperty("tareas_pendientes")
    private Integer tareasPendientes;

    @JsonProperty("tareas_completadas")
    private Integer tareasCompletadas;

    // Constructores
    public OrdenTrabajoDTO() {
    }

    // Getters and Setters
    public UUID getIdOrdenTrabajo() {
        return idOrdenTrabajo;
    }

    public void setIdOrdenTrabajo(UUID idOrdenTrabajo) {
        this.idOrdenTrabajo = idOrdenTrabajo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public UsuarioBasicDTO getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(UsuarioBasicDTO tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public List<TareaBasicDTO> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaBasicDTO> tareas) {
        this.tareas = tareas;
    }

    public Integer getTotalTareas() {
        return totalTareas;
    }

    public void setTotalTareas(Integer totalTareas) {
        this.totalTareas = totalTareas;
    }

    public Integer getTareasPendientes() {
        return tareasPendientes;
    }

    public void setTareasPendientes(Integer tareasPendientes) {
        this.tareasPendientes = tareasPendientes;
    }

    public Integer getTareasCompletadas() {
        return tareasCompletadas;
    }

    public void setTareasCompletadas(Integer tareasCompletadas) {
        this.tareasCompletadas = tareasCompletadas;
    }
}
