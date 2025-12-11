package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que relaciona un análisis con los recursos necesarios
 * (equipos, reactivos o insumos) y la cantidad requerida
 */
@Entity
@Table(name = "analisis_recurso")
public class AnalisisRecurso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "analisis_recurso_id")
    private UUID analisisRecursoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analisis_id", nullable = false)
    private Analisis analisis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reactivo_id")
    private Reactivo reactivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;

    @Column(name = "cantidad_requerida", precision = 10, scale = 3)
    private BigDecimal cantidadRequerida;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public AnalisisRecurso() {
    }

    public AnalisisRecurso(Analisis analisis, BigDecimal cantidadRequerida) {
        this.analisis = analisis;
        this.cantidadRequerida = cantidadRequerida;
    }

    // Getters y Setters
    public UUID getAnalisisRecursoId() {
        return analisisRecursoId;
    }

    public void setAnalisisRecursoId(UUID analisisRecursoId) {
        this.analisisRecursoId = analisisRecursoId;
    }

    public Analisis getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Reactivo getReactivo() {
        return reactivo;
    }

    public void setReactivo(Reactivo reactivo) {
        this.reactivo = reactivo;
    }

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public BigDecimal getCantidadRequerida() {
        return cantidadRequerida;
    }

    public void setCantidadRequerida(BigDecimal cantidadRequerida) {
        this.cantidadRequerida = cantidadRequerida;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}
