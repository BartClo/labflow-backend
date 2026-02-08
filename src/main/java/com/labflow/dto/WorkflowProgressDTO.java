package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * DTO representing the overall workflow progress of an Orden de Trabajo.
 * Provides a summary view of all stages and completion status.
 */
public class WorkflowProgressDTO {

    @JsonProperty("etapas")
    private List<OrdenTrabajoEtapaDTO> etapas;

    @JsonProperty("etapa_actual")
    private String etapaActual;

    @JsonProperty("etapa_actual_orden")
    private Integer etapaActualOrden;

    @JsonProperty("porcentaje_completado")
    private Double porcentajeCompletado;

    @JsonProperty("total_etapas")
    private Integer totalEtapas;

    @JsonProperty("etapas_completadas")
    private Integer etapasCompletadas;

    @JsonProperty("workflow_completo")
    private Boolean workflowCompleto;

    // Constructors
    public WorkflowProgressDTO() {
    }

    public WorkflowProgressDTO(List<OrdenTrabajoEtapaDTO> etapas) {
        this.etapas = etapas;
        this.totalEtapas = etapas.size();
        this.etapasCompletadas = (int) etapas.stream()
                .filter(e -> "COMPLETADO".equals(e.getEstadoEtapa()))
                .count();
        this.workflowCompleto = this.etapasCompletadas.equals(this.totalEtapas);
        this.porcentajeCompletado = totalEtapas > 0 
                ? (etapasCompletadas * 100.0) / totalEtapas 
                : 0.0;
        
        // Find current stage (first non-completed)
        etapas.stream()
                .filter(e -> !"COMPLETADO".equals(e.getEstadoEtapa()))
                .findFirst()
                .ifPresent(e -> {
                    this.etapaActual = e.getNombreEtapa();
                    this.etapaActualOrden = e.getOrdenSecuencia();
                });
    }

    // Getters and Setters
    public List<OrdenTrabajoEtapaDTO> getEtapas() {
        return etapas;
    }

    public void setEtapas(List<OrdenTrabajoEtapaDTO> etapas) {
        this.etapas = etapas;
    }

    public String getEtapaActual() {
        return etapaActual;
    }

    public void setEtapaActual(String etapaActual) {
        this.etapaActual = etapaActual;
    }

    public Integer getEtapaActualOrden() {
        return etapaActualOrden;
    }

    public void setEtapaActualOrden(Integer etapaActualOrden) {
        this.etapaActualOrden = etapaActualOrden;
    }

    public Double getPorcentajeCompletado() {
        return porcentajeCompletado;
    }

    public void setPorcentajeCompletado(Double porcentajeCompletado) {
        this.porcentajeCompletado = porcentajeCompletado;
    }

    public Integer getTotalEtapas() {
        return totalEtapas;
    }

    public void setTotalEtapas(Integer totalEtapas) {
        this.totalEtapas = totalEtapas;
    }

    public Integer getEtapasCompletadas() {
        return etapasCompletadas;
    }

    public void setEtapasCompletadas(Integer etapasCompletadas) {
        this.etapasCompletadas = etapasCompletadas;
    }

    public Boolean getWorkflowCompleto() {
        return workflowCompleto;
    }

    public void setWorkflowCompleto(Boolean workflowCompleto) {
        this.workflowCompleto = workflowCompleto;
    }
}
