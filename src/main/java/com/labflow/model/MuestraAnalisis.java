package com.labflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Entidad que representa la relación many-to-many entre Muestra y Análisis
 * Incluye información específica del análisis para cada muestra
 */
@Entity
@Table(name = "muestra_analisis")
public class MuestraAnalisis {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_muestra_analisis")
    private UUID idMuestraAnalisis;

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_muestra", nullable = false)
    @NotNull(message = "La muestra es requerida")
    @JsonBackReference
    private Muestra muestra;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_analisis", nullable = false)
    @NotNull(message = "El análisis es requerido")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Analisis analisis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_trabajo_id", nullable = true)
    @JsonBackReference("ordenTrabajo-tareas")
    private OrdenTrabajo ordenTrabajo;

    // Estado específico del análisis para esta muestra
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_analisis", length = 50)
    private EstadoAnalisis estadoAnalisis = EstadoAnalisis.PENDIENTE;

    // Orden de ejecución de los análisis
    @Column(name = "orden_ejecucion")
    @Min(value = 1, message = "El orden de ejecución debe ser mayor a 0")
    private Integer ordenEjecucion = 1;

    // Resultados y fechas
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "resultado", columnDefinition = "jsonb")
    private Map<String, Object> resultado;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_finalizacion")
    private LocalDateTime fechaFinalizacion;

    @Column(name = "tecnico_responsable", length = 255)
    @Size(max = 255, message = "El técnico responsable no puede exceder 255 caracteres")
    private String tecnicoResponsable;

    // Observaciones específicas del análisis
    @Column(name = "observaciones_analisis", columnDefinition = "TEXT")
    private String observacionesAnalisis;

    @Column(name = "cumple_normativa")
    private Boolean cumpleNormativa;

    @Column(name = "cumple_norma")
    private Boolean cumpleNorma;

    @Column(name = "es_control_calidad")
    private Boolean esControlCalidad = false;

    @Column(name = "notas_validacion", columnDefinition = "TEXT")
    private String notasValidacion;

    // Metadatos
    @Column(name = "fecha_agregado", updatable = false)
    private LocalDateTime fechaAgregado;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Enum para estado del análisis
    public enum EstadoAnalisis {
        PENDIENTE, EN_PROCESO, COMPLETADO, VALIDADO, CANCELADO
    }

    // Constructors
    public MuestraAnalisis() {
        this.fechaAgregado = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public MuestraAnalisis(Muestra muestra, Analisis analisis) {
        this();
        this.muestra = muestra;
        this.analisis = analisis;
    }

    public MuestraAnalisis(Muestra muestra, Analisis analisis, Integer orden) {
        this(muestra, analisis);
        this.ordenEjecucion = orden;
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaAgregado == null) {
            this.fechaAgregado = LocalDateTime.now();
        }
        if (this.fechaActualizacion == null) {
            this.fechaActualizacion = LocalDateTime.now();
        }
    }

    // Getters and Setters
    public UUID getIdMuestraAnalisis() {
        return idMuestraAnalisis;
    }

    public void setIdMuestraAnalisis(UUID idMuestraAnalisis) {
        this.idMuestraAnalisis = idMuestraAnalisis;
    }

    public Muestra getMuestra() {
        return muestra;
    }

    public void setMuestra(Muestra muestra) {
        this.muestra = muestra;
    }

    public Analisis getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }

    public EstadoAnalisis getEstadoAnalisis() {
        return estadoAnalisis;
    }

    public void setEstadoAnalisis(EstadoAnalisis estadoAnalisis) {
        this.estadoAnalisis = estadoAnalisis;
    }

    public Integer getOrdenEjecucion() {
        return ordenEjecucion;
    }

    public void setOrdenEjecucion(Integer ordenEjecucion) {
        this.ordenEjecucion = ordenEjecucion;
    }

    public Map<String, Object> getResultado() {
        return resultado;
    }

    public void setResultado(Map<String, Object> resultado) {
        this.resultado = resultado;
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

    public String getObservacionesAnalisis() {
        return observacionesAnalisis;
    }

    public void setObservacionesAnalisis(String observacionesAnalisis) {
        this.observacionesAnalisis = observacionesAnalisis;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public Boolean getCumpleNormativa() {
        return cumpleNormativa;
    }

    public void setCumpleNormativa(Boolean cumpleNormativa) {
        this.cumpleNormativa = cumpleNormativa;
    }

    public Boolean getEsControlCalidad() {
        return esControlCalidad;
    }

    public void setEsControlCalidad(Boolean esControlCalidad) {
        this.esControlCalidad = esControlCalidad;
    }

    public String getNotasValidacion() {
        return notasValidacion;
    }

    public void setNotasValidacion(String notasValidacion) {
        this.notasValidacion = notasValidacion;
    }

    public Boolean getCumpleNorma() {
        return cumpleNorma;
    }

    public void setCumpleNorma(Boolean cumpleNorma) {
        this.cumpleNorma = cumpleNorma;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    // Métodos de utilidad
    public boolean estaCompletado() {
        return this.estadoAnalisis == EstadoAnalisis.COMPLETADO;
    }

    public boolean estaPendiente() {
        return this.estadoAnalisis == EstadoAnalisis.PENDIENTE;
    }

    public boolean estaEnProceso() {
        return this.estadoAnalisis == EstadoAnalisis.EN_PROCESO;
    }

    public boolean estaCancelado() {
        return this.estadoAnalisis == EstadoAnalisis.CANCELADO;
    }

    public boolean estaValidado() {
        return this.estadoAnalisis == EstadoAnalisis.VALIDADO;
    }

    public void iniciarAnalisis(String tecnico) {
        this.estadoAnalisis = EstadoAnalisis.EN_PROCESO;
        this.fechaInicio = LocalDateTime.now();
        this.tecnicoResponsable = tecnico;
    }

    public void completarAnalisis(Map<String, Object> resultados) {
        this.estadoAnalisis = EstadoAnalisis.COMPLETADO;
        this.fechaFinalizacion = LocalDateTime.now();
        this.resultado = resultados;
    }

    public void cancelarAnalisis(String motivo) {
        this.estadoAnalisis = EstadoAnalisis.CANCELADO;
        this.observacionesAnalisis = motivo;
    }

    public void validar() {
        if (this.estadoAnalisis != EstadoAnalisis.COMPLETADO) {
            throw new IllegalStateException("Solo se pueden validar tareas en estado COMPLETADO");
        }
        this.estadoAnalisis = EstadoAnalisis.VALIDADO;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getDuracionEnMinutos() {
        if (fechaInicio != null && fechaFinalizacion != null) {
            return java.time.Duration.between(fechaInicio, fechaFinalizacion).toMinutes();
        }
        return null;
    }

    @Override
    public String toString() {
        return "MuestraAnalisis{" +
                "idMuestraAnalisis=" + idMuestraAnalisis +
                ", muestra=" + (muestra != null ? muestra.getNumeroInterno() : "null") +
                ", analisis=" + (analisis != null ? analisis.getNombreAnalisis() : "null") +
                ", estadoAnalisis=" + estadoAnalisis +
                ", ordenEjecucion=" + ordenEjecucion +
                '}';
    }
}