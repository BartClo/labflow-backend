package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para respuesta de insumo de laboratorio
 */
@Schema(description = "Información completa de un insumo de laboratorio")
public class InsumoDTO {

    @Schema(description = "ID único del insumo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID insumoId;

    @Schema(description = "Nombre del insumo", example = "Placa Petri")
    private String nombre;

    @Schema(description = "Marca del insumo", example = "Pyrex")
    private String marca;

    @Schema(description = "Descripción detallada")
    private String descripcion;

    @Schema(description = "Stock actual", example = "150.000")
    private BigDecimal stockActual;

    @Schema(description = "Stock mínimo", example = "50.000")
    private BigDecimal stockMinimo;

    @Schema(description = "Unidad de medida", example = "unidades")
    private String unidadMedida;

    @Schema(description = "Ubicación de almacenamiento", example = "Estantería A - Nivel 3")
    private String ubicacionAlmacenamiento;

    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del insumo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

    // Getters y Setters
    public UUID getInsumoId() {
        return insumoId;
    }

    public void setInsumoId(UUID insumoId) {
        this.insumoId = insumoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
