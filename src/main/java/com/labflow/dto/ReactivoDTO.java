package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para respuesta de reactivo químico
 */
@Schema(description = "Información completa de un reactivo químico")
public class ReactivoDTO {

    @Schema(description = "ID único del reactivo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID reactivoId;

    @Schema(description = "Nombre del reactivo", example = "Ácido Clorhídrico")
    private String nombre;

    @Schema(description = "Código único del reactivo", example = "HCL-001")
    private String codigo;

    @Schema(description = "Número CAS", example = "7647-01-0")
    private String numeroCas;

    @Schema(description = "Marca del reactivo", example = "Merck")
    private String marca;

    @Schema(description = "Número de lote", example = "L2025001")
    private String lote;

    @Schema(description = "Concentración", example = "37% p/p")
    private String concentracion;

    @Schema(description = "Fecha de vencimiento", example = "2026-12-31")
    private LocalDate fechaVencimiento;

    @Schema(description = "Fecha de apertura del reactivo", example = "2025-01-15")
    private LocalDate fechaApertura;

    @Schema(description = "Stock actual", example = "2.500")
    private BigDecimal stockActual;

    @Schema(description = "Stock mínimo", example = "0.500")
    private BigDecimal stockMinimo;

    @Schema(description = "Unidad de medida", example = "L")
    private String unidadMedida;

    @Schema(description = "Ubicación de almacenamiento", example = "Armario de ácidos - Estante 2")
    private String ubicacionAlmacenamiento;

    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del reactivo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

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
