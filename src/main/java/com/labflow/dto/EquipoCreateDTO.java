package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * DTO para crear un nuevo equipo de laboratorio
 */
@Schema(description = "Datos para crear un nuevo equipo de laboratorio")
public class EquipoCreateDTO {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(min = 3, max = 200, message = "El nombre debe tener entre 3 y 200 caracteres")
    @Schema(description = "Nombre del equipo", example = "Espectrofotómetro UV-Vis")
    private String nombre;

    @Size(max = 100, message = "El modelo no puede exceder 100 caracteres")
    @Schema(description = "Modelo del equipo", example = "Genesys 10S")
    private String modelo;

    @Size(max = 150, message = "El número de serie no puede exceder 150 caracteres")
    @Schema(description = "Número de serie", example = "A1B2C3D4E5")
    private String numeroSerie;

    @Size(max = 200, message = "La ubicación no puede exceder 200 caracteres")
    @Schema(description = "Ubicación física del equipo", example = "Laboratorio 2 - Mesa 3")
    private String ubicacion;

    @Schema(description = "Fecha de última calibración", example = "2025-10-15")
    private LocalDate fechaCalibracion;

    @Schema(description = "Fecha de próxima calibración", example = "2026-10-15")
    private LocalDate proximaCalibracion;

    @Size(max = 2000, message = "Las observaciones no pueden exceder 2000 caracteres")
    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del equipo", example = "true")
    private Boolean activo = true;

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getFechaCalibracion() {
        return fechaCalibracion;
    }

    public void setFechaCalibracion(LocalDate fechaCalibracion) {
        this.fechaCalibracion = fechaCalibracion;
    }

    public LocalDate getProximaCalibracion() {
        return proximaCalibracion;
    }

    public void setProximaCalibracion(LocalDate proximaCalibracion) {
        this.proximaCalibracion = proximaCalibracion;
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
