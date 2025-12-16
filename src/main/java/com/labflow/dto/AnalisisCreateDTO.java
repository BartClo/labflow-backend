package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * DTO para crear un nuevo análisis
 */
@Schema(description = "Datos para crear un nuevo análisis de laboratorio")
public class AnalisisCreateDTO {

    @NotBlank(message = "El nombre del análisis es obligatorio")
    @Size(min = 2, max = 255, message = "El nombre debe tener entre 2 y 255 caracteres")
    @Schema(description = "Nombre del análisis", example = "Determinación de pH")
    private String nombreAnalisis;

    @NotBlank(message = "El código del análisis es obligatorio")
    @Size(min = 2, max = 50, message = "El código debe tener entre 2 y 50 caracteres")
    @Schema(description = "Código único del análisis", example = "AN-001")
    private String codigo;

    @Schema(description = "Descripción detallada del análisis", example = "Determinación del pH en muestras líquidas y sólidas")
    private String descripcion;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    @Schema(description = "Categoría del análisis", example = "Físico-Químico")
    private String categoria;

    @Size(max = 255, message = "El método de ensayo no puede exceder 255 caracteres")
    @Schema(description = "Método de ensayo utilizado", example = "NCh 409/1, APHA 4500")
    private String metodoEnsayo;

    @Schema(description = "Tipos de muestra donde se puede aplicar el análisis", 
            example = "[\"Agua\", \"Suelo\", \"Alimentos\"]")
    private List<String> tiposMuestraAplicables;

    @Schema(description = "Parámetros a medir con sus características en formato JSON")
    private Map<String, Object> parametrosMedir;

    @Schema(description = "Equipos requeridos para el análisis en formato JSON")
    private Map<String, Object> equiposRequeridos;

    @Schema(description = "Insumos requeridos para el análisis en formato JSON")
    private Map<String, Object> insumosRequeridos;

    @Schema(description = "Reactivos requeridos para el análisis en formato JSON")
    private Map<String, Object> reactivosRequeridos;

    @DecimalMin(value = "0.0", inclusive = false, message = "La duración debe ser mayor a 0")
    @Digits(integer = 3, fraction = 2, message = "La duración debe tener máximo 3 enteros y 2 decimales")
    @Schema(description = "Duración estimada en horas", example = "2.5")
    private BigDecimal duracionEstimadaHoras;

    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    @Schema(description = "Precio en pesos chilenos", example = "25000.00")
    private BigDecimal precioClp;

    @Min(value = 1, message = "Los días de entrega deben ser al menos 1")
    @Schema(description = "Días estimados para entrega de resultados", example = "3")
    private Integer diasEntrega;

    @Pattern(regexp = "^(Activo|Inactivo|En Desarrollo)$", 
             message = "El estado debe ser: Activo, Inactivo o En Desarrollo")
    @Schema(description = "Estado del análisis", example = "Activo", 
            allowableValues = {"Activo", "Inactivo", "En Desarrollo"})
    private String estado = "Activo";

    // Constructor por defecto
    public AnalisisCreateDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
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

    public Map<String, Object> getInsumosRequeridos() {
        return insumosRequeridos;
    }

    public void setInsumosRequeridos(Map<String, Object> insumosRequeridos) {
        this.insumosRequeridos = insumosRequeridos;
    }

    public Map<String, Object> getReactivosRequeridos() {
        return reactivosRequeridos;
    }

    public void setReactivosRequeridos(Map<String, Object> reactivosRequeridos) {
        this.reactivosRequeridos = reactivosRequeridos;
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

    public Integer getDiasEntrega() {
        return diasEntrega;
    }

    public void setDiasEntrega(Integer diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}