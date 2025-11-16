package com.labflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolDTO {

    private UUID id;
    private String nombre;
    private String descripcion;
    private String[] permisos;
    private Boolean activo;
    private Long cantidadUsuarios;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
