package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad PlantillaAnalisis que representa la relación muchos a muchos entre Plantilla y Analisis
 */
@Entity
@Table(name = "plantilla_analisis")
public class PlantillaAnalisis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_plantilla_analisis", updatable = false, nullable = false)
    private UUID idPlantillaAnalisis;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private Plantilla plantilla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analisis", nullable = false)
    private Analisis analisis;

    @Column(name = "orden_en_plantilla", nullable = false)
    private Integer ordenEnPlantilla = 1;

    @CreationTimestamp
    @Column(name = "fecha_agregado", nullable = false)
    private LocalDateTime fechaAgregado;

    // Constructores
    public PlantillaAnalisis() {
        // Constructor vacío requerido por JPA
    }

    public PlantillaAnalisis(Plantilla plantilla, Analisis analisis, Integer ordenEnPlantilla) {
        this.plantilla = plantilla;
        this.analisis = analisis;
        this.ordenEnPlantilla = ordenEnPlantilla;
    }

    // Getters y Setters
    public UUID getIdPlantillaAnalisis() {
        return idPlantillaAnalisis;
    }

    public void setIdPlantillaAnalisis(UUID idPlantillaAnalisis) {
        this.idPlantillaAnalisis = idPlantillaAnalisis;
    }

    public Plantilla getPlantilla() {
        return plantilla;
    }

    public void setPlantilla(Plantilla plantilla) {
        this.plantilla = plantilla;
    }

    public Analisis getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }

    public Integer getOrdenEnPlantilla() {
        return ordenEnPlantilla;
    }

    public void setOrdenEnPlantilla(Integer ordenEnPlantilla) {
        this.ordenEnPlantilla = ordenEnPlantilla;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    // toString
    @Override
    public String toString() {
        return "PlantillaAnalisis{" +
                "idPlantillaAnalisis=" + idPlantillaAnalisis +
                ", ordenEnPlantilla=" + ordenEnPlantilla +
                ", fechaAgregado=" + fechaAgregado +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlantillaAnalisis that = (PlantillaAnalisis) o;
        return idPlantillaAnalisis != null && idPlantillaAnalisis.equals(that.idPlantillaAnalisis);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}