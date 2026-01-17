package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO para mostrar tareas pendientes disponibles para asignar a una orden de trabajo
 */
public class TareaPendienteDTO {

    @JsonProperty("id_muestra_analisis")
    private UUID idMuestraAnalisis;

    @JsonProperty("numero_muestra")
    private String numeroMuestra;

    @JsonProperty("codigo_barras")
    private String codigoBarras;

    @JsonProperty("nombre_analisis")
    private String nombreAnalisis;

    @JsonProperty("codigo_analisis")
    private String codigoAnalisis;

    @JsonProperty("fecha_agregado")
    private LocalDateTime fechaAgregado;

    @JsonProperty("prioridad")
    private String prioridad;

    @JsonProperty("cliente")
    private ClienteBasicDTO cliente;

    // Constructores
    public TareaPendienteDTO() {
    }

    // Getters and Setters
    public UUID getIdMuestraAnalisis() {
        return idMuestraAnalisis;
    }

    public void setIdMuestraAnalisis(UUID idMuestraAnalisis) {
        this.idMuestraAnalisis = idMuestraAnalisis;
    }

    public String getNumeroMuestra() {
        return numeroMuestra;
    }

    public void setNumeroMuestra(String numeroMuestra) {
        this.numeroMuestra = numeroMuestra;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getNombreAnalisis() {
        return nombreAnalisis;
    }

    public void setNombreAnalisis(String nombreAnalisis) {
        this.nombreAnalisis = nombreAnalisis;
    }

    public String getCodigoAnalisis() {
        return codigoAnalisis;
    }

    public void setCodigoAnalisis(String codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }

    public LocalDateTime getFechaAgregado() {
        return fechaAgregado;
    }

    public void setFechaAgregado(LocalDateTime fechaAgregado) {
        this.fechaAgregado = fechaAgregado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public ClienteBasicDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBasicDTO cliente) {
        this.cliente = cliente;
    }

    // Nested DTO for Cliente
    public static class ClienteBasicDTO {
        @JsonProperty("id_cliente")
        private UUID idCliente;

        @JsonProperty("nombre")
        private String nombre;

        @JsonProperty("razon_social")
        private String razonSocial;

        public ClienteBasicDTO() {
        }

        public UUID getIdCliente() {
            return idCliente;
        }

        public void setIdCliente(UUID idCliente) {
            this.idCliente = idCliente;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRazonSocial() {
            return razonSocial;
        }

        public void setRazonSocial(String razonSocial) {
            this.razonSocial = razonSocial;
        }
    }
}
