package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de respuesta de inicio de sesión")
public class LoginResponseDTO {

    @Schema(description = "Indica si el login fue exitoso", example = "true")
    private Boolean success;

    @Schema(description = "Mensaje de respuesta", example = "Login exitoso")
    private String message;

    @Schema(description = "Información del usuario autenticado")
    private UsuarioDTO user;

    // Constructor para login exitoso
    public LoginResponseDTO(UsuarioDTO usuario) {
        this.success = true;
        this.message = "Login exitoso";
        this.user = usuario;
    }

    // Constructor para login fallido
    public static LoginResponseDTO failed(String message) {
        LoginResponseDTO response = new LoginResponseDTO();
        response.setSuccess(false);
        response.setMessage(message);
        return response;
    }
}
