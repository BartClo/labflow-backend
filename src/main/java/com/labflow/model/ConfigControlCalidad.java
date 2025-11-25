package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad ConfigControlCalidad que representa la configuración de control de calidad
 * para un parámetro específico según NCh 409
 */
@Entity
@Table(name = "config_control_calidad")
public class ConfigControlCalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parametro_id", nullable = false)
    private Parametro parametro;

    @Column(name = "tipo_control", nullable = false, length = 100)
    private String tipoControl;

    @Column(name = "recuperacion_min", precision = 5, scale = 2)
    private BigDecimal recuperacionMin;

    @Column(name = "recuperacion_max", precision = 5, scale = 2)
    private BigDecimal recuperacionMax;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructores
    public ConfigControlCalidad() {
    }

    public ConfigControlCalidad(Parametro parametro, String tipoControl, BigDecimal recuperacionMin, BigDecimal recuperacionMax) {
        this.parametro = parametro;
        this.tipoControl = tipoControl;
        this.recuperacionMin = recuperacionMin;
        this.recuperacionMax = recuperacionMax;
    }

    // Getters y Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Parametro getParametro() {
        return parametro;
    }

    public void setParametro(Parametro parametro) {
        this.parametro = parametro;
    }

    public String getTipoControl() {
        return tipoControl;
    }

    public void setTipoControl(String tipoControl) {
        this.tipoControl = tipoControl;
    }

    public BigDecimal getRecuperacionMin() {
        return recuperacionMin;
    }

    public void setRecuperacionMin(BigDecimal recuperacionMin) {
        this.recuperacionMin = recuperacionMin;
    }

    public BigDecimal getRecuperacionMax() {
        return recuperacionMax;
    }

    public void setRecuperacionMax(BigDecimal recuperacionMax) {
        this.recuperacionMax = recuperacionMax;
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

    @Override
    public String toString() {
        return "ConfigControlCalidad{" +
                "id=" + id +
                ", tipoControl='" + tipoControl + '\'' +
                ", recuperacionMin=" + recuperacionMin +
                ", recuperacionMax=" + recuperacionMax +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigControlCalidad that = (ConfigControlCalidad) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
