package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO para crear un nuevo reactivo químico
 */
@Schema(description = "Datos para crear un nuevo reactivo químico")
public class ReactivoCreateDTO {

    @NotBlank(message = "El nombre del reactivo es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    @Schema(description = "Nombre del reactivo", example = "Ácido Clorhídrico")
    private String nombre;

    @Size(max = 100, message = "El código no puede exceder 100 caracteres")
    @Schema(description = "Código único del reactivo", example = "HCL-001")
    private String codigo;

    @Size(max = 50, message = "El número CAS no puede exceder 50 caracteres")
    @Schema(description = "Número CAS", example = "7647-01-0")
    private String numeroCas;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    @Schema(description = "Marca del reactivo", example = "Merck")
    private String marca;

    @Size(max = 100, message = "El lote no puede exceder 100 caracteres")
    @Schema(description = "Número de lote", example = "L2025001")
    private String lote;

    @Size(max = 100, message = "La concentración no puede exceder 100 caracteres")
    @Schema(description = "Concentración", example = "37% p/p")
    private String concentracion;

    @Schema(description = "Fecha de vencimiento", example = "2026-12-31")
    private LocalDate fechaVencimiento;

    @Schema(description = "Fecha de apertura del reactivo", example = "2025-01-15")
    private LocalDate fechaApertura;

    @DecimalMin(value = "0.0", message = "El stock actual debe ser mayor o igual a 0")
    @Digits(integer = 7, fraction = 3, message = "El stock debe tener máximo 7 enteros y 3 decimales")
    @Schema(description = "Stock actual", example = "2.500")
    private BigDecimal stockActual;

    @DecimalMin(value = "0.0", message = "El stock mínimo debe ser mayor o igual a 0")
    @Digits(integer = 7, fraction = 3, message = "El stock debe tener máximo 7 enteros y 3 decimales")
    @Schema(description = "Stock mínimo", example = "0.500")
    private BigDecimal stockMinimo;

    @Size(max = 50, message = "La unidad de medida no puede exceder 50 caracteres")
    @Schema(description = "Unidad de medida", example = "L")
    private String unidadMedida;

    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    @Schema(description = "Ubicación de almacenamiento", example = "Armario de ácidos - Estante 2")
    private String ubicacionAlmacenamiento;

    @Size(max = 2000, message = "Las observaciones no pueden exceder 2000 caracteres")
    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del reactivo", example = "true")
    private Boolean activo = true;

    // Getters y Setters
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
}
