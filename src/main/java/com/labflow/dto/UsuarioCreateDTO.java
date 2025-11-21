package com.labflow.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para crear un nuevo usuario")
public class UsuarioCreateDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Schema(description = "Nombre del usuario", example = "Juan", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
    @Schema(description = "Apellido del usuario", example = "Pérez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser válido")
    @Size(max = 255, message = "El email no puede exceder 255 caracteres")
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@labflow.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El username solo puede contener letras, números, puntos, guiones y guiones bajos")
    @Schema(description = "Nombre de usuario para inicio de sesión", example = "juan.perez", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 255, message = "La contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Contraseña del usuario (mínimo 8 caracteres)", example = "Password123!", requiredMode = Schema.RequiredMode.REQUIRED, format = "password")
    private String password;

    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "El teléfono solo puede contener números y símbolos +, -, (), espacios")
    @Schema(description = "Número de teléfono del usuario", example = "+56912345678")
    private String telefono;

    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    @Schema(description = "Dirección del usuario", example = "Av. Principal 123, Santiago")
    private String direccion;

    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    @Schema(description = "Fecha de nacimiento del usuario", example = "1990-01-15")
    private LocalDate fechaNacimiento;

    @NotNull(message = "El rol es obligatorio")
    @Schema(description = "ID del rol asignado al usuario", example = "123e4567-e89b-12d3-a456-426614174000", requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID rolId;

    @Schema(description = "Estado activo del usuario", example = "true", defaultValue = "true")
    private Boolean activo = true;
}
