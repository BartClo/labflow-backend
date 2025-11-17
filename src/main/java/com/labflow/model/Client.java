package com.labflow.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad Client que representa un cliente en el sistema LabFlow
 */
@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cliente", updatable = false, nullable = false)
    private UUID idCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 255)
    private String nombreCliente;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "empresa", length = 200)
    private String empresa;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "telefono", length = 20)
    private String telefono;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "persona_contacto", length = 100)
    private String personaContacto;

    @Column(name = "tipo_cliente", length = 50)
    private String tipoCliente = "Empresa";

    @Column(name = "activo")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // equals y hashCode basados en id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return idCliente != null && idCliente.equals(client.idCliente);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}