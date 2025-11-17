package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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

    @Schema(description = "Nombre completo", example = "Juan Pérez")
    private String nombre;

    @Schema(description = "Nombre de la empresa", example = "Empresa ABC S.A.")
    private String empresa;

    @Email
    @Schema(description = "Correo electrónico", example = "contacto@empresaabc.com")
    private String email;

    @Schema(description = "Número de teléfono", example = "+56 9 1234 5678")
    private String telefono;

    @Schema(description = "Dirección completa", example = "Av. Principal 123, Santiago")
    private String direccion;

    @Schema(description = "Persona de contacto", example = "María González")
    private String personaContacto;

    @Schema(description = "Tipo de cliente", example = "Empresa")
    private String tipoCliente;

    @Schema(description = "Estado activo del cliente", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha de creación del registro", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última actualización", example = "2025-10-07T14:30:00")
    private LocalDateTime fechaActualizacion;

    // Constructor por defecto
    public ClienteDTO() {}

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPersonaContacto() {
        return personaContacto;
    }

    public void setPersonaContacto(String personaContacto) {
        this.personaContacto = personaContacto;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
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