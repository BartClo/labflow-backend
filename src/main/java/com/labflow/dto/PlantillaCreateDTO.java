package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * DTO para crear una nueva plantilla de procedimientos
 */
@Schema(description = "Datos para crear una nueva plantilla de procedimientos")
public class PlantillaCreateDTO {

    @NotBlank(message = "El nombre de la plantilla es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    @Schema(description = "Nombre de la plantilla", example = "Análisis Completo de Agua Potable")
    private String nombrePlantilla;

    @Size(max = 2000, message = "La descripción no puede exceder 2000 caracteres")
    @Schema(description = "Descripción detallada de la plantilla", 
            example = "Plantilla estándar para análisis completo de calidad de agua potable según normativas chilenas")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(Activo|Inactivo)$", message = "El estado debe ser 'Activo' o 'Inactivo'")
    @Schema(description = "Estado de la plantilla", example = "Activo", allowableValues = {"Activo", "Inactivo"})
    private String estado = "Activo";

    @NotEmpty(message = "Debe especificar al menos un tipo de muestra aplicable")
    @Schema(description = "Tipos de muestra donde aplica esta plantilla", 
            example = "[\"Agua\", \"Suelo\"]")
    private List<@Pattern(regexp = "^(Agua|Suelo|Aire|Alimentos|Otros)$", 
                         message = "Tipo de muestra debe ser: Agua, Suelo, Aire, Alimentos u Otros") String> tiposMuestraAplicables;

    @Schema(description = "Lista de IDs de análisis incluidos en la plantilla con su orden",
            example = "[{\"analisisId\": \"123e4567-e89b-12d3-a456-426614174000\", \"orden\": 1}]")
    private List<AnalisisEnPlantillaDTO> analisisIncluidos;

    @Schema(description = "Indica si es un paquete comercial", example = "false")
    private Boolean esPaqueteComercial = false;

    @Digits(integer = 13, fraction = 2, message = "El precio debe tener máximo 13 enteros y 2 decimales")
    @Schema(description = "Precio del paquete comercial (requerido si esPaqueteComercial=true)", example = "150000.00")
    private BigDecimal precioPaquete;

    @Size(max = 100, message = "El código del paquete no puede exceder 100 caracteres")
    @Schema(description = "Código del paquete comercial", example = "PKG-AGUA-001")
    private String codigoPaquete;

    // Constructor por defecto
    public PlantillaCreateDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
    public String getNombrePlantilla() {
        return nombrePlantilla;
    }

    public void setNombrePlantilla(String nombrePlantilla) {
        this.nombrePlantilla = nombrePlantilla;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<String> getTiposMuestraAplicables() {
        return tiposMuestraAplicables;
    }

    public void setTiposMuestraAplicables(List<String> tiposMuestraAplicables) {
        this.tiposMuestraAplicables = tiposMuestraAplicables;
    }

    public List<AnalisisEnPlantillaDTO> getAnalisisIncluidos() {
        return analisisIncluidos;
    }

    public void setAnalisisIncluidos(List<AnalisisEnPlantillaDTO> analisisIncluidos) {
        this.analisisIncluidos = analisisIncluidos;
    }

    public Boolean getEsPaqueteComercial() {
        return esPaqueteComercial;
    }

    public void setEsPaqueteComercial(Boolean esPaqueteComercial) {
        this.esPaqueteComercial = esPaqueteComercial;
    }

    public BigDecimal getPrecioPaquete() {
        return precioPaquete;
    }

    public void setPrecioPaquete(BigDecimal precioPaquete) {
        this.precioPaquete = precioPaquete;
    }

    public String getCodigoPaquete() {
        return codigoPaquete;
    }

    public void setCodigoPaquete(String codigoPaquete) {
        this.codigoPaquete = codigoPaquete;
    }

    /**
     * DTO anidado para representar un análisis dentro de una plantilla
     */
    @Schema(description = "Análisis incluido en la plantilla con su orden de ejecución")
    public static class AnalisisEnPlantillaDTO {
        
        @NotNull(message = "El ID del análisis es obligatorio")
        @Schema(description = "ID del análisis", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID analisisId;

        @Min(value = 1, message = "El orden debe ser mayor a 0")
        @Schema(description = "Orden de ejecución en la plantilla", example = "1")
        private Integer orden = 1;

        // Constructor por defecto
        public AnalisisEnPlantillaDTO() {
            // Constructor vacío requerido para serialización JSON
        }

        public AnalisisEnPlantillaDTO(UUID analisisId, Integer orden) {
            this.analisisId = analisisId;
            this.orden = orden;
        }

        public UUID getAnalisisId() {
            return analisisId;
        }

        public void setAnalisisId(UUID analisisId) {
            this.analisisId = analisisId;
        }

        public Integer getOrden() {
            return orden;
        }

        public void setOrden(Integer orden) {
            this.orden = orden;
        }
    }
}