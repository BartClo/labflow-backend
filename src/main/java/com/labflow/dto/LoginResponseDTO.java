package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta exitosa de inicio de sesión")
public class LoginResponseDTO {

    @Schema(description = "Indica si el login fue exitoso", example = "true")
    private Boolean success;

    @Schema(description = "Mensaje de respuesta", example = "Login exitoso")
    private String message;

    @Schema(description = "ID del usuario", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID userId;

    @Schema(description = "Nombre completo del usuario", example = "Juan Pérez")
    private String nombreCompleto;

    @Schema(description = "Username del usuario", example = "juan.perez")
    private String username;

    @Schema(description = "Email del usuario", example = "juan.perez@labflow.com")
    private String email;

    @Schema(description = "Información del rol asignado")
    private RolDTO rol;

    @Schema(description = "Estado activo del usuario", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha y hora del login", example = "2025-11-21T16:30:00")
    private LocalDateTime loginTime;

    // Constructor para login exitoso
    public LoginResponseDTO(UsuarioDTO usuario) {
        this.success = true;
        this.message = "Login exitoso";
        this.userId = usuario.getId();
        this.nombreCompleto = usuario.getNombreCompleto();
        this.username = usuario.getUsername();
        this.email = usuario.getEmail();
        this.rol = usuario.getRol();
        this.activo = usuario.getActivo();
        this.loginTime = LocalDateTime.now();
    }

    // Constructor para login fallido
    public static LoginResponseDTO failed(String message) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setSuccess(false);
        response.setMessage(message);
        response.setLoginTime(LocalDateTime.now());
        return response;
    }
}
