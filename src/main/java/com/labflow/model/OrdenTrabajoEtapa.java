package com.labflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity representing a workflow stage in an Orden de Trabajo.
 * Each OT has 4 sequential stages that must be completed in order.
 */
@Entity
@Table(name = "orden_trabajo_etapas")
public class OrdenTrabajoEtapa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_etapa")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_trabajo_id", nullable = false)
    private OrdenTrabajo ordenTrabajo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_etapa", nullable = false, length = 50)
    private TipoEtapaWorkflow tipoEtapa;

    @Column(name = "orden_secuencia", nullable = false)
    private Integer ordenSecuencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_etapa", nullable = false, length = 20)
    private EstadoEtapa estadoEtapa = EstadoEtapa.PENDIENTE;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_completado")
    private LocalDateTime fechaCompletado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_asignado_id")
    private Usuario tecnicoAsignado;

    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public OrdenTrabajoEtapa() {
    }

    public OrdenTrabajoEtapa(OrdenTrabajo ordenTrabajo, TipoEtapaWorkflow tipoEtapa, Usuario tecnicoAsignado) {
        this.ordenTrabajo = ordenTrabajo;
        this.tipoEtapa = tipoEtapa;
        this.ordenSecuencia = tipoEtapa.getOrden();
        this.tecnicoAsignado = tecnicoAsignado;
        this.estadoEtapa = EstadoEtapa.PENDIENTE;
    }

    // Business Methods

    /**
     * Initiates the stage, marking it as in progress and recording the start time.
     * @throws IllegalStateException if the stage is not in PENDIENTE state
     */
    public void iniciar() {
        if (!estadoEtapa.puedeIniciar()) {
            throw new IllegalStateException(
                String.format("No se puede iniciar la etapa %s. Estado actual: %s", 
                    tipoEtapa.getDisplayName(), estadoEtapa.getDisplayName())
            );
        }
        this.estadoEtapa = EstadoEtapa.EN_PROGRESO;
        this.fechaInicio = LocalDateTime.now();
    }

    /**
     * Completes the stage, marking it as completed and recording the completion time.
     * @param notas Optional notes about the stage completion
     * @throws IllegalStateException if the stage is not in EN_PROGRESO state
     */
    public void completar(String notas) {
        if (!estadoEtapa.puedeCompletar()) {
            throw new IllegalStateException(
                String.format("No se puede completar la etapa %s. Estado actual: %s", 
                    tipoEtapa.getDisplayName(), estadoEtapa.getDisplayName())
            );
        }
        this.estadoEtapa = EstadoEtapa.COMPLETADO;
        this.fechaCompletado = LocalDateTime.now();
        if (notas != null && !notas.trim().isEmpty()) {
            this.notas = notas;
        }
    }

    /**
     * Checks if this stage is currently the active stage (EN_PROGRESO).
     */
    public boolean estaActiva() {
        return estadoEtapa == EstadoEtapa.EN_PROGRESO;
    }

    /**
     * Checks if this stage has been completed.
     */
    public boolean estaCompletada() {
        return estadoEtapa == EstadoEtapa.COMPLETADO;
    }

    /**
     * Checks if this stage is pending to start.
     */
    public boolean estaPendiente() {
        return estadoEtapa == EstadoEtapa.PENDIENTE;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public TipoEtapaWorkflow getTipoEtapa() {
        return tipoEtapa;
    }

    public void setTipoEtapa(TipoEtapaWorkflow tipoEtapa) {
        this.tipoEtapa = tipoEtapa;
        if (tipoEtapa != null) {
            this.ordenSecuencia = tipoEtapa.getOrden();
        }
    }

    public Integer getOrdenSecuencia() {
        return ordenSecuencia;
    }

    public void setOrdenSecuencia(Integer ordenSecuencia) {
        this.ordenSecuencia = ordenSecuencia;
    }

    public EstadoEtapa getEstadoEtapa() {
        return estadoEtapa;
    }

    public void setEstadoEtapa(EstadoEtapa estadoEtapa) {
        this.estadoEtapa = estadoEtapa;
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

    public Usuario getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(Usuario tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
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
