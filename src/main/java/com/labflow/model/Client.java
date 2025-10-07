package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad Client que representa un cliente en el sistema LabFlow
 */
@Entity
@Table(name = "clientes")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_cliente", updatable = false, nullable = false)
    private UUID idCliente;

    @Column(name = "nombre_cliente", nullable = false, length = 255)
    private String nombreCliente;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Client() {
    }

    public Client(String nombreCliente) {
        this.nombreCliente = nombreCliente;
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

    // toString
    @Override
    public String toString() {
        return "Client{" +
                "idCliente=" + idCliente +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaActualizacion=" + fechaActualizacion +
                '}';
    }

    // equals y hashCode
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