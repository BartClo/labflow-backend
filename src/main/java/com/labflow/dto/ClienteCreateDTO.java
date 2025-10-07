package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un nuevo cliente
 */
@Schema(description = "Datos para crear un nuevo cliente")
public class ClienteCreateDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del cliente", example = "Empresa ABC S.A.")
    private String nombreCliente;

    // Constructor por defecto
    public ClienteCreateDTO() {}

    // Constructor con parámetros
    public ClienteCreateDTO(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    // Getters y Setters
    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
}