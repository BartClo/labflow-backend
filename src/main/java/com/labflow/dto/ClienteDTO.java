package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para respuesta de cliente
 */
@Schema(description = "Información completa de un cliente")
public class ClienteDTO {

    @Schema(description = "ID único del cliente", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID idCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del cliente", example = "Empresa ABC S.A.")
    private String nombreCliente;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime updatedAt;

    // Constructor por defecto
    public ClienteDTO() {}

    // Constructor con parámetros
    public ClienteDTO(UUID idCliente, String nombreCliente, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}