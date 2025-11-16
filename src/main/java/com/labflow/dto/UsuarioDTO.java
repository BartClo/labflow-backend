package com.labflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private UUID id;
    private String nombre;
    private String apellido;
    private String nombreCompleto;
    private String email;
    private String username;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private RolDTO rol;
    private Boolean activo;
    private LocalDateTime ultimaConexion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
