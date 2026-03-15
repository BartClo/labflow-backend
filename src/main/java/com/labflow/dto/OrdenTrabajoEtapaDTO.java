package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO representing a workflow stage in an Orden de Trabajo.
 */
public class OrdenTrabajoEtapaDTO {

    @JsonProperty("id_etapa")
    private UUID idEtapa;

    @JsonProperty("tipo_etapa")
    private String tipoEtapa;

    @JsonProperty("nombre_etapa")
    private String nombreEtapa;

    @JsonProperty("orden_secuencia")
    private Integer ordenSecuencia;

    @JsonProperty("estado_etapa")
    private String estadoEtapa;

    @JsonProperty("estado_etapa_display")
    private String estadoEtapaDisplay;

    @JsonProperty("fecha_inicio")
    private LocalDateTime fechaInicio;

    @JsonProperty("fecha_completado")
    private LocalDateTime fechaCompletado;

    @JsonProperty("tecnico_asignado")
    private String tecnicoAsignado;

    @JsonProperty("tecnico_asignado_id")
    private UUID tecnicoAsignadoId;

    @JsonProperty("notas")
    private String notas;

    @JsonProperty("valor")
    private String valor;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
    public UUID getIdEtapa() {
        return idEtapa;
    }

    public void setIdEtapa(UUID idEtapa) {
        this.idEtapa = idEtapa;
    }

    public String getTipoEtapa() {
        return tipoEtapa;
    }

    public void setTipoEtapa(String tipoEtapa) {
        this.tipoEtapa = tipoEtapa;
    }

    public String getNombreEtapa() {
        return nombreEtapa;
    }

    public void setNombreEtapa(String nombreEtapa) {
        this.nombreEtapa = nombreEtapa;
    }

    public Integer getOrdenSecuencia() {
        return ordenSecuencia;
    }

    public void setOrdenSecuencia(Integer ordenSecuencia) {
        this.ordenSecuencia = ordenSecuencia;
    }

    public String getEstadoEtapa() {
        return estadoEtapa;
    }

    public void setEstadoEtapa(String estadoEtapa) {
        this.estadoEtapa = estadoEtapa;
    }

    public String getEstadoEtapaDisplay() {
        return estadoEtapaDisplay;
    }

    public void setEstadoEtapaDisplay(String estadoEtapaDisplay) {
        this.estadoEtapaDisplay = estadoEtapaDisplay;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaCompletado() {
        return fechaCompletado;
    }

    public void setFechaCompletado(LocalDateTime fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public String getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(String tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

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

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
