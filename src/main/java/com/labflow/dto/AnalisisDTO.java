package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO para respuesta de análisis
 */
@Schema(description = "Información completa de un análisis de laboratorio")
public class AnalisisDTO {

    @Schema(description = "ID único del análisis", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idAnalisis;

    @Schema(description = "Nombre del análisis", example = "Determinación de pH")
    private String nombreAnalisis;

    @Schema(description = "Código único del análisis", example = "AN-001")
    private String codigo;

    @Schema(description = "Descripción detallada del análisis", 
            example = "Determinación del pH en muestras líquidas y sólidas")
    private String descripcion;

    @Schema(description = "Categoría del análisis", example = "Físico-Químico")
    private String categoria;

    @Schema(description = "Método de ensayo utilizado", example = "NCh 409/1, APHA 4500")
    private String metodoEnsayo;

    @Schema(description = "Tipos de muestra donde se puede aplicar el análisis", 
            example = "[\"Agua\", \"Suelo\", \"Alimentos\"]")
    private List<String> tiposMuestraAplicables;

    @Schema(description = "Parámetros a medir con sus características")
    private Map<String, Object> parametrosMedir;

    @Schema(description = "Equipos requeridos para el análisis")
    private Map<String, Object> equiposRequeridos;

    @Schema(description = "Duración estimada en horas", example = "2.5")
    private BigDecimal duracionEstimadaHoras;

    @Schema(description = "Precio en pesos chilenos", example = "25000.00")
    private BigDecimal precioClp;

    @Schema(description = "Estado del análisis", example = "Activo")
    private String estado;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

    // Constructor por defecto
    public AnalisisDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
    public UUID getIdAnalisis() {
        return idAnalisis;
    }

    public void setIdAnalisis(UUID idAnalisis) {
        this.idAnalisis = idAnalisis;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMetodoEnsayo() {
        return metodoEnsayo;
    }

    public void setMetodoEnsayo(String metodoEnsayo) {
        this.metodoEnsayo = metodoEnsayo;
    }

    public List<String> getTiposMuestraAplicables() {
        return tiposMuestraAplicables;
    }

    public void setTiposMuestraAplicables(List<String> tiposMuestraAplicables) {
        this.tiposMuestraAplicables = tiposMuestraAplicables;
    }

    public Map<String, Object> getParametrosMedir() {
        return parametrosMedir;
    }

    public void setParametrosMedir(Map<String, Object> parametrosMedir) {
        this.parametrosMedir = parametrosMedir;
    }

    public Map<String, Object> getEquiposRequeridos() {
        return equiposRequeridos;
    }

    public void setEquiposRequeridos(Map<String, Object> equiposRequeridos) {
        this.equiposRequeridos = equiposRequeridos;
    }

    public BigDecimal getDuracionEstimadaHoras() {
        return duracionEstimadaHoras;
    }

    public void setDuracionEstimadaHoras(BigDecimal duracionEstimadaHoras) {
        this.duracionEstimadaHoras = duracionEstimadaHoras;
    }

    public BigDecimal getPrecioClp() {
        return precioClp;
    }

    public void setPrecioClp(BigDecimal precioClp) {
        this.precioClp = precioClp;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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