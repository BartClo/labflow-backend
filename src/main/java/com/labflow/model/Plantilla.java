package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Entidad Plantilla que representa una plantilla de procedimientos en el sistema LabFlow
 */
@Entity
@Table(name = "plantillas")
public class Plantilla {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_plantilla", updatable = false, nullable = false)
    private UUID idPlantilla;

    @Column(name = "nombre_plantilla", nullable = false, length = 255)
    private String nombrePlantilla;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "Activo";

    @Column(name = "tipos_muestra_aplicables", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> tiposMuestraAplicables;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Relación muchos a muchos con Analisis a través de la tabla intermedia
    @OneToMany(mappedBy = "plantilla", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PlantillaAnalisis> plantillaAnalisis;

    // Constructores
    public Plantilla() {
        // Constructor vacío requerido por JPA
    }

    public Plantilla(String nombrePlantilla, String descripcion, String estado, List<String> tiposMuestraAplicables) {
        this.nombrePlantilla = nombrePlantilla;
        this.descripcion = descripcion;
        this.estado = estado;
        this.tiposMuestraAplicables = tiposMuestraAplicables;
    }

    // Getters y Setters
    public UUID getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(UUID idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

    public String getNombrePlantilla() {
        return nombrePlantilla;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getTiposMuestraAplicables() {
        return tiposMuestraAplicables;
    }

    public void setTiposMuestraAplicables(List<String> tiposMuestraAplicables) {
        this.tiposMuestraAplicables = tiposMuestraAplicables;
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

    public List<PlantillaAnalisis> getPlantillaAnalisis() {
        return plantillaAnalisis;
    }

    public void setPlantillaAnalisis(List<PlantillaAnalisis> plantillaAnalisis) {
        this.plantillaAnalisis = plantillaAnalisis;
    }

    // toString
    @Override
    public String toString() {
        return "Plantilla{" +
                "idPlantilla=" + idPlantilla +
                ", nombrePlantilla='" + nombrePlantilla + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado='" + estado + '\'' +
                ", tiposMuestraAplicables=" + tiposMuestraAplicables +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plantilla plantilla = (Plantilla) o;
        return idPlantilla != null && idPlantilla.equals(plantilla.idPlantilla);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}