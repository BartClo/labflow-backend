package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa un reactivo químico
 * Incluye control de vencimiento y stock
 */
@Entity
@Table(name = "reactivo")
public class Reactivo {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "reactivo_id")
    private UUID reactivoId;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "codigo", unique = true, length = 100)
    private String codigo;

    @Column(name = "numero_cas", length = 50)
    private String numeroCas;

    @Column(name = "marca", length = 100)
    private String marca;

    @Column(name = "lote", length = 100)
    private String lote;

    @Column(name = "concentracion", length = 100)
    private String concentracion;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_apertura")
    private LocalDate fechaApertura;

    @Column(name = "stock_actual", precision = 10, scale = 3)
    private BigDecimal stockActual;

    @Column(name = "stock_minimo", precision = 10, scale = 3)
    private BigDecimal stockMinimo;

    @Column(name = "unidad_medida", length = 50)
    private String unidadMedida;

    @Column(name = "ubicacion_almacenamiento", length = 200)
    private String ubicacionAlmacenamiento;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Reactivo() {
    }

    public Reactivo(String nombre, String codigo, String numeroCas, String concentracion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.numeroCas = numeroCas;
        this.concentracion = concentracion;
        this.activo = true;
    }

    // Constructor simplificado para el DataSeeder
    public Reactivo(String nombre, String lote, LocalDate fechaVencimiento, BigDecimal stockActual) {
        this.nombre = nombre;
        this.lote = lote;
        this.fechaVencimiento = fechaVencimiento;
        this.stockActual = stockActual;
        this.codigo = "RE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.activo = true;
    }

    // Getters y Setters
    public UUID getReactivoId() {
        return reactivoId;
    }

    public void setReactivoId(UUID reactivoId) {
        this.reactivoId = reactivoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNumeroCas() {
        return numeroCas;
    }

    public void setNumeroCas(String numeroCas) {
        this.numeroCas = numeroCas;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getUbicacionAlmacenamiento() {
        return ubicacionAlmacenamiento;
    }

    public void setUbicacionAlmacenamiento(String ubicacionAlmacenamiento) {
        this.ubicacionAlmacenamiento = ubicacionAlmacenamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
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
