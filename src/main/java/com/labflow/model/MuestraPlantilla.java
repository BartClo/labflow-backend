package com.labflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa la relación many-to-many entre Muestra y Plantilla
 * Incluye información específica de la plantilla para cada muestra
 */
@Entity
@Table(name = "muestra_plantilla")
public class MuestraPlantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_muestra_plantilla")
    private UUID idMuestraPlantilla;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_muestra", nullable = false)
    @NotNull(message = "La muestra es requerida")
    @JsonBackReference
    private Muestra muestra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_plantilla", nullable = false)
    @NotNull(message = "La plantilla es requerida")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Plantilla plantilla;

    // Estado específico de la plantilla para esta muestra
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_plantilla", length = 50)
    private EstadoPlantilla estadoPlantilla = EstadoPlantilla.PENDIENTE;

    // Orden de ejecución de las plantillas
    @Column(name = "orden_ejecucion")
    @Min(value = 1, message = "El orden de ejecución debe ser mayor a 0")
    private Integer ordenEjecucion = 1;

    // Fechas de ejecución
    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "tecnico_responsable", length = 255)
    @Size(max = 255, message = "El técnico responsable no puede exceder 255 caracteres")
    private String tecnicoResponsable;

    // Observaciones específicas de la plantilla
    @Column(name = "observaciones_plantilla", columnDefinition = "TEXT")
    private String observacionesPlantilla;

    // Metadatos
    @Column(name = "fecha_agregada", updatable = false)
    private LocalDateTime fechaAgregada;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Enum para estado de la plantilla
    public enum EstadoPlantilla {
        PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA
    }

    // Constructors
    public MuestraPlantilla() {
        this.fechaAgregada = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public MuestraPlantilla(Muestra muestra, Plantilla plantilla) {
        this();
        this.muestra = muestra;
        this.plantilla = plantilla;
    }

    public MuestraPlantilla(Muestra muestra, Plantilla plantilla, Integer orden) {
        this(muestra, plantilla);
        this.ordenEjecucion = orden;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaAgregada == null) {
            this.fechaAgregada = LocalDateTime.now();
        }
        if (this.fechaActualizacion == null) {
            this.fechaActualizacion = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public UUID getIdMuestraPlantilla() {
        return idMuestraPlantilla;
    }

    public void setIdMuestraPlantilla(UUID idMuestraPlantilla) {
        this.idMuestraPlantilla = idMuestraPlantilla;
    }

    public Muestra getMuestra() {
        return muestra;
    }

    public void setMuestra(Muestra muestra) {
        this.muestra = muestra;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public EstadoPlantilla getEstadoPlantilla() {
        return estadoPlantilla;
    }

    public void setEstadoPlantilla(EstadoPlantilla estadoPlantilla) {
        this.estadoPlantilla = estadoPlantilla;
    }

    public Integer getOrdenEjecucion() {
        return ordenEjecucion;
    }

    public void setOrdenEjecucion(Integer ordenEjecucion) {
        this.ordenEjecucion = ordenEjecucion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public void setTecnicoResponsable(String tecnicoResponsable) {
        this.tecnicoResponsable = tecnicoResponsable;
    }

    public String getObservacionesPlantilla() {
        return observacionesPlantilla;
    }

    public void setObservacionesPlantilla(String observacionesPlantilla) {
        this.observacionesPlantilla = observacionesPlantilla;
    }

    public LocalDateTime getFechaAgregada() {
        return fechaAgregada;
    }

    public void setFechaAgregada(LocalDateTime fechaAgregada) {
        this.fechaAgregada = fechaAgregada;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    // Métodos de utilidad
    public boolean estaCompletada() {
        return this.estadoPlantilla == EstadoPlantilla.COMPLETADA;
    }

    public boolean estaPendiente() {
        return this.estadoPlantilla == EstadoPlantilla.PENDIENTE;
    }

    public boolean estaEnProceso() {
        return this.estadoPlantilla == EstadoPlantilla.EN_PROCESO;
    }

    public boolean estaCancelada() {
        return this.estadoPlantilla == EstadoPlantilla.CANCELADA;
    }

    public void iniciarPlantilla(String tecnico) {
        this.estadoPlantilla = EstadoPlantilla.EN_PROCESO;
        this.fechaInicio = LocalDateTime.now();
        this.tecnicoResponsable = tecnico;
    }

    public void completarPlantilla() {
        this.estadoPlantilla = EstadoPlantilla.COMPLETADA;
        this.fechaFinalizacion = LocalDateTime.now();
    }

    public void cancelarPlantilla(String motivo) {
        this.estadoPlantilla = EstadoPlantilla.CANCELADA;
        this.observacionesPlantilla = motivo;
    }

    public Long getDuracionEnMinutos() {
        if (fechaInicio != null && fechaFinalizacion != null) {
            return java.time.Duration.between(fechaInicio, fechaFinalizacion).toMinutes();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MuestraPlantilla{" +
                "idMuestraPlantilla=" + idMuestraPlantilla +
                ", muestra=" + (muestra != null ? muestra.getNumeroInterno() : "null") +
                ", plantilla=" + (plantilla != null ? plantilla.getNombrePlantilla() : "null") +
                ", estadoPlantilla=" + estadoPlantilla +
                ", ordenEjecucion=" + ordenEjecucion +
                '}';
    }
}