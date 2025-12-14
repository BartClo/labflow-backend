package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO para crear un nuevo insumo de laboratorio
 */
@Schema(description = "Datos para crear un nuevo insumo de laboratorio")
public class InsumoCreateDTO {

    @NotBlank(message = "El nombre del insumo es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    @Schema(description = "Nombre del insumo", example = "Placa Petri")
    private String nombre;

    @Size(max = 100, message = "La marca no puede exceder 100 caracteres")
    @Schema(description = "Marca del insumo", example = "Pyrex")
    private String marca;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Descripción detallada")
    private String descripcion;

    @DecimalMin(value = "0.0", message = "El stock actual debe ser mayor o igual a 0")
    @Digits(integer = 7, fraction = 3, message = "El stock debe tener máximo 7 enteros y 3 decimales")
    @Schema(description = "Stock actual", example = "150.000")
    private BigDecimal stockActual;

    @DecimalMin(value = "0.0", message = "El stock mínimo debe ser mayor o igual a 0")
    @Digits(integer = 7, fraction = 3, message = "El stock debe tener máximo 7 enteros y 3 decimales")
    @Schema(description = "Stock mínimo", example = "50.000")
    private BigDecimal stockMinimo;

    @Size(max = 50, message = "La unidad de medida no puede exceder 50 caracteres")
    @Schema(description = "Unidad de medida", example = "unidades")
    private String unidadMedida;

    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    @Schema(description = "Ubicación de almacenamiento", example = "Estantería A - Nivel 3")
    private String ubicacionAlmacenamiento;

    @Size(max = 2000, message = "Las observaciones no pueden exceder 2000 caracteres")
    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del insumo", example = "true")
    private Boolean activo = true;

    // Getters y Setters
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
}
