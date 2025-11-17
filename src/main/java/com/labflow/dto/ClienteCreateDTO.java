package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear un nuevo cliente
 */
@Schema(description = "Datos para crear un nuevo cliente")
public class ClienteCreateDTO {

    @NotBlank(message = "El nombre del cliente es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre completo del cliente o persona de contacto", example = "Juan Pérez")
    private String nombreCliente;

    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    @Schema(description = "Nombre completo", example = "Juan Pérez")
    private String nombre;

    @Size(max = 200, message = "El nombre de la empresa debe tener máximo 200 caracteres")
    @Schema(description = "Nombre de la empresa", example = "Empresa ABC S.A.")
    private String empresa;

    @Email(message = "El email debe ser válido")
    @Size(max = 100, message = "El email debe tener máximo 100 caracteres")
    @Schema(description = "Correo electrónico", example = "contacto@empresaabc.com")
    private String email;

    @Size(max = 20, message = "El teléfono debe tener máximo 20 caracteres")
    @Schema(description = "Número de teléfono", example = "+56 9 1234 5678")
    private String telefono;

    @Size(max = 255, message = "La dirección debe tener máximo 255 caracteres")
    @Schema(description = "Dirección completa", example = "Av. Principal 123, Santiago")
    private String direccion;

    @Size(max = 100, message = "El nombre de la persona de contacto debe tener máximo 100 caracteres")
    @Schema(description = "Persona de contacto", example = "María González")
    private String personaContacto;

    @Size(max = 50, message = "El tipo de cliente debe tener máximo 50 caracteres")
    @Schema(description = "Tipo de cliente", example = "Empresa", allowableValues = {"Empresa", "Persona", "Gobierno", "ONG"})
    private String tipoCliente;

    @Schema(description = "Estado activo del cliente", example = "true")
    private Boolean activo;

    // Constructor por defecto
    public ClienteCreateDTO() {
        this.tipoCliente = "Empresa";
        this.activo = true;
    }

    // Getters y Setters
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
}