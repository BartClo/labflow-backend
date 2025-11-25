package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entidad Parametro que representa un parámetro medido en un análisis de laboratorio
 */
@Entity
@Table(name = "parametro")
public class Parametro {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analisis_id", nullable = false)
    private Analisis analisis;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "unidad", length = 50)
    private String unidad;

    @Column(name = "valor_maximo_normativa", precision = 15, scale = 6)
    private BigDecimal valorMaximoNormativa;

    @OneToMany(mappedBy = "parametro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ConfigControlCalidad> configuracionesControl = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructores
    public Parametro() {
    }

    public Parametro(Analisis analisis, String nombre, String unidad, BigDecimal valorMaximoNormativa) {
        this.analisis = analisis;
        this.nombre = nombre;
        this.unidad = unidad;
        this.valorMaximoNormativa = valorMaximoNormativa;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Analisis getAnalisis() {
        return analisis;
    }

    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public BigDecimal getValorMaximoNormativa() {
        return valorMaximoNormativa;
    }

    public void setValorMaximoNormativa(BigDecimal valorMaximoNormativa) {
        this.valorMaximoNormativa = valorMaximoNormativa;
    }

    public List<ConfigControlCalidad> getConfiguracionesControl() {
        return configuracionesControl;
    }

    public void setConfiguracionesControl(List<ConfigControlCalidad> configuracionesControl) {
        this.configuracionesControl = configuracionesControl;
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

    // Métodos de utilidad
    public void addConfiguracionControl(ConfigControlCalidad config) {
        configuracionesControl.add(config);
        config.setParametro(this);
    }

    public void removeConfiguracionControl(ConfigControlCalidad config) {
        configuracionesControl.remove(config);
        config.setParametro(null);
    }

    @Override
    public String toString() {
        return "Parametro{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", unidad='" + unidad + '\'' +
                ", valorMaximoNormativa=" + valorMaximoNormativa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parametro parametro = (Parametro) o;
        return id != null && id.equals(parametro.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
