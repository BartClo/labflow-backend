package com.labflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representa una Orden de Trabajo que agrupa varias tareas (MuestraAnalisis)
 * para ser procesadas por un técnico específico.
 */
@Entity
@Table(name = "ordenes_trabajo")
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_orden_trabajo")
    private UUID id;

    @Column(name = "codigo_ot", unique = true, nullable = false, length = 50)
    private String codigoOT;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoOT estado;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tecnico_asignado_id", nullable = false)
    private Usuario tecnicoAsignado;

    @OneToMany(mappedBy = "ordenTrabajo", fetch = FetchType.LAZY)
    private List<MuestraAnalisis> tareas = new ArrayList<>();

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructors
    public OrdenTrabajo() {
        this.estado = EstadoOT.ABIERTA;
        this.fechaCreacion = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCodigoOT() {
        return codigoOT;
    }

    public void setCodigoOT(String codigoOT) {
        this.codigoOT = codigoOT;
    }

    public EstadoOT getEstado() {
        return estado;
    }

    public void setEstado(EstadoOT estado) {
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

    public Usuario getTecnicoAsignado() {
        return tecnicoAsignado;
    }

    public void setTecnicoAsignado(Usuario tecnicoAsignado) {
        this.tecnicoAsignado = tecnicoAsignado;
    }

    public List<MuestraAnalisis> getTareas() {
        return tareas;
    }

    public void setTareas(List<MuestraAnalisis> tareas) {
        this.tareas = tareas;
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

    // Business Methods
    
    /**
     * Completa la orden de trabajo estableciendo la fecha de finalización
     */
    public void completar() {
        this.estado = EstadoOT.FINALIZADA;
        this.fechaFinalizacion = LocalDateTime.now();
    }

    /**
     * Cancela la orden de trabajo
     */
    public void cancelar() {
        this.estado = EstadoOT.CANCELADA;
        this.fechaFinalizacion = LocalDateTime.now();
    }

    /**
     * Inicia el procesamiento de la orden
     */
    public void iniciar() {
        this.estado = EstadoOT.EN_PROCESO;
    }

    /**
     * Verifica si la orden tiene tareas pendientes
     */
    public boolean tieneTareasPendientes() {
        return tareas.stream()
                .anyMatch(tarea -> tarea.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE);
    }

    /**
     * Cuenta las tareas pendientes
     */
    public long contarTareasPendientes() {
        return tareas.stream()
                .filter(tarea -> tarea.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE)
                .count();
    }
}
