package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para respuesta de plantilla de procedimientos
 */
@Schema(description = "Información completa de una plantilla de procedimientos")
public class PlantillaDTO {

    @Schema(description = "ID único de la plantilla", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idPlantilla;

    @Schema(description = "Nombre de la plantilla", example = "Análisis Completo de Agua Potable")
    private String nombrePlantilla;

    @Schema(description = "Descripción detallada de la plantilla", 
            example = "Plantilla estándar para análisis completo de calidad de agua potable según normativas chilenas")
    private String descripcion;

    @Schema(description = "Estado de la plantilla", example = "Activo")
    private String estado;

    @Schema(description = "Tipos de muestra donde aplica esta plantilla", 
            example = "[\"Agua\", \"Suelo\"]")
    private List<String> tiposMuestraAplicables;

    @Schema(description = "Lista de análisis incluidos en la plantilla")
    private List<AnalisisEnPlantillaResponseDTO> analisisIncluidos;

    @Schema(description = "Fecha de creación de la plantilla", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

    // Constructor por defecto
    public PlantillaDTO() {
        // Constructor vacío requerido para serialización JSON
    }

    // Getters y Setters
    public UUID getIdPlantilla() {
        return idPlantilla;
    }

    public void setIdPlantilla(UUID idPlantilla) {
        this.idPlantilla = idPlantilla;
    }

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

    public List<AnalisisEnPlantillaResponseDTO> getAnalisisIncluidos() {
        return analisisIncluidos;
    }

    public void setAnalisisIncluidos(List<AnalisisEnPlantillaResponseDTO> analisisIncluidos) {
        this.analisisIncluidos = analisisIncluidos;
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

    /**
     * DTO anidado para representar un análisis dentro de una plantilla con información completa
     */
    @Schema(description = "Análisis incluido en la plantilla con información detallada")
    public static class AnalisisEnPlantillaResponseDTO {
        
        @Schema(description = "ID del análisis", example = "123e4567-e89b-12d3-a456-426614174000")
        private UUID analisisId;

        @Schema(description = "Código del análisis", example = "PH-001")
        private String codigo;

        @Schema(description = "Nombre del análisis", example = "Determinación de pH")
        private String nombreAnalisis;

        @Schema(description = "Categoría del análisis", example = "Físico-Químico")
        private String categoria;

        @Schema(description = "Orden de ejecución en la plantilla", example = "1")
        private Integer ordenEnPlantilla;

        @Schema(description = "Fecha cuando se agregó a la plantilla", example = "2025-10-07T14:30:00")
        private LocalDateTime fechaAgregado;

        // Constructor por defecto
        public AnalisisEnPlantillaResponseDTO() {
            // Constructor vacío requerido para serialización JSON
        }

        // Getters y Setters
        public UUID getAnalisisId() {
            return analisisId;
        }

        public void setAnalisisId(UUID analisisId) {
            this.analisisId = analisisId;
        }

        public String getCodigo() {
            return codigo;
        }

        public void setCodigo(String codigo) {
            this.codigo = codigo;
        }

        public String getNombreAnalisis() {
            return nombreAnalisis;
        }

        public void setNombreAnalisis(String nombreAnalisis) {
            this.nombreAnalisis = nombreAnalisis;
        }

        public String getCategoria() {
            return categoria;
        }

        public void setCategoria(String categoria) {
            this.categoria = categoria;
        }

        public Integer getOrdenEnPlantilla() {
            return ordenEnPlantilla;
        }

        public void setOrdenEnPlantilla(Integer ordenEnPlantilla) {
            this.ordenEnPlantilla = ordenEnPlantilla;
        }

        public LocalDateTime getFechaAgregado() {
            return fechaAgregado;
        }

        public void setFechaAgregado(LocalDateTime fechaAgregado) {
            this.fechaAgregado = fechaAgregado;
        }
    }
}