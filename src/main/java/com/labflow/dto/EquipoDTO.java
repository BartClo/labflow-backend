package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para respuesta de equipo de laboratorio
 */
@Schema(description = "Información completa de un equipo de laboratorio")
public class EquipoDTO {

    @Schema(description = "ID único del equipo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID equipoId;

    @Schema(description = "Nombre del equipo", example = "Espectrofotómetro UV-Vis")
    private String nombre;

    @Schema(description = "Código único del equipo", example = "ESP-001")
    private String codigo;

    @Schema(description = "Marca del equipo", example = "Thermo Scientific")
    private String marca;

    @Schema(description = "Modelo del equipo", example = "Genesys 10S")
    private String modelo;

    @Schema(description = "Número de serie", example = "A1B2C3D4E5")
    private String numeroSerie;

    @Schema(description = "Ubicación física del equipo", example = "Laboratorio 2 - Mesa 3")
    private String ubicacion;

    @Schema(description = "Fecha de última calibración", example = "2025-10-15")
    private LocalDate fechaCalibracion;

    @Schema(description = "Fecha de próxima calibración", example = "2026-10-15")
    private LocalDate proximaCalibracion;

    @Schema(description = "Observaciones adicionales")
    private String observaciones;

    @Schema(description = "Estado activo del equipo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

    // Getters y Setters
    public UUID getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(UUID equipoId) {
        this.equipoId = equipoId;
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

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
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
