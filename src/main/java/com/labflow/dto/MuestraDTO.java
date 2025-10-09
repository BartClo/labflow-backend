package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para respuestas de muestras
 * Incluye toda la información de la muestra y sus análisis asociados
 */
public class MuestraDTO {

    @JsonProperty("id_muestra")
    private UUID idMuestra;

    @JsonProperty("numero_interno")
    private String numeroInterno;

    @JsonProperty("codigo_barras")
    private String codigoBarras;

    // Información del cliente
    @JsonProperty("cliente")
    private ClienteBasicDTO cliente;

    @JsonProperty("punto_muestreo")
    private String puntoMuestreo;

    @JsonProperty("tipo_muestra")
    private String tipoMuestra;

    @JsonProperty("prioridad")
    private String prioridad;

    @JsonProperty("estado")
    private String estado;

    // Fechas
    @JsonProperty("fecha_muestreo")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaMuestreo;

    @JsonProperty("fecha_recepcion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRecepcion;

    @JsonProperty("fecha_creacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaCreacion;

    @JsonProperty("fecha_actualizacion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaActualizacion;

    @JsonProperty("responsable_muestreo")
    private String responsableMuestreo;

    // Condiciones de transporte
    @JsonProperty("temperatura_transporte")
    private BigDecimal temperaturaTransporte;

    @JsonProperty("condiciones_transporte")
    private String condicionesTransporte;

    @JsonProperty("tipo_envase")
    private String tipoEnvase;

    @JsonProperty("conservantes_utilizados")
    private Boolean conservantesUtilizados;

    @JsonProperty("descripcion_conservantes")
    private String descripcionConservantes;

    @JsonProperty("observaciones")
    private String observaciones;

    // Datos adicionales
    @JsonProperty("info_cliente")
    private String infoCliente;

    @JsonProperty("metodo_analisis")
    private String metodoAnalisis;

    @JsonProperty("volumen_muestra")
    private BigDecimal volumenMuestra;

    @JsonProperty("unidad_volumen")
    private String unidadVolumen;

    // Análisis asociados
    @JsonProperty("analisis")
    private List<MuestraAnalisisDTO> analisis;

    // Estadísticas
    @JsonProperty("total_analisis")
    private Integer totalAnalisis;

    @JsonProperty("analisis_completados")
    private Integer analisisCompletados;

    @JsonProperty("progreso_porcentaje")
    private Double progresoPorcentaje;

    // Constructor por defecto requerido para deserialización JSON
    public MuestraDTO() {
        // Constructor vacío necesario para Jackson JSON binding
    }

    // DTO anidado para información básica del cliente
    public static class ClienteBasicDTO {
        @JsonProperty("id_cliente")
        private UUID idCliente;

        @JsonProperty("nombre")
        private String nombre;

        @JsonProperty("rut")
        private String rut;

        @JsonProperty("email")
        private String email;

        // Constructor por defecto requerido para deserialización JSON
        public ClienteBasicDTO() {
            // Constructor vacío necesario para Jackson JSON binding
        }

        public ClienteBasicDTO(UUID idCliente, String nombre, String rut, String email) {
            this.idCliente = idCliente;
            this.nombre = nombre;
            this.rut = rut;
            this.email = email;
        }

        // Getters and Setters
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

        public String getRut() {
            return rut;
        }

        public void setRut(String rut) {
            this.rut = rut;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    // DTO anidado para información de análisis asociados
    public static class MuestraAnalisisDTO {
        @JsonProperty("id_muestra_analisis")
        private UUID idMuestraAnalisis;

        @JsonProperty("id_analisis")
        private UUID idAnalisis;

        @JsonProperty("nombre_analisis")
        private String nombreAnalisis;

        @JsonProperty("estado_analisis")
        private String estadoAnalisis;

        @JsonProperty("orden_ejecucion")
        private Integer ordenEjecucion;

        @JsonProperty("fecha_inicio")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime fechaInicio;

        @JsonProperty("fecha_finalizacion")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime fechaFinalizacion;

        @JsonProperty("tecnico_responsable")
        private String tecnicoResponsable;

        @JsonProperty("observaciones_analisis")
        private String observacionesAnalisis;

        // Constructor por defecto requerido para deserialización JSON
        public MuestraAnalisisDTO() {
            // Constructor vacío necesario para Jackson JSON binding
        }

        // Getters and Setters
        public UUID getIdMuestraAnalisis() {
            return idMuestraAnalisis;
        }

        public void setIdMuestraAnalisis(UUID idMuestraAnalisis) {
            this.idMuestraAnalisis = idMuestraAnalisis;
        }

        public UUID getIdAnalisis() {
            return idAnalisis;
        }

        public void setIdAnalisis(UUID idAnalisis) {
            this.idAnalisis = idAnalisis;
        }

        public String getNombreAnalisis() {
            return nombreAnalisis;
        }

        public void setNombreAnalisis(String nombreAnalisis) {
            this.nombreAnalisis = nombreAnalisis;
        }

        public String getEstadoAnalisis() {
            return estadoAnalisis;
        }

        public void setEstadoAnalisis(String estadoAnalisis) {
            this.estadoAnalisis = estadoAnalisis;
        }

        public Integer getOrdenEjecucion() {
            return ordenEjecucion;
        }

        public void setOrdenEjecucion(Integer ordenEjecucion) {
            this.ordenEjecucion = ordenEjecucion;
        }

        public LocalDateTime getFechaInicio() {
            return fechaInicio;
        }

        public void setFechaInicio(LocalDateTime fechaInicio) {
            this.fechaInicio = fechaInicio;
        }

        public LocalDateTime getFechaFinalizacion() {
            return fechaFinalizacion;
        }

        public void setFechaFinalizacion(LocalDateTime fechaFinalizacion) {
            this.fechaFinalizacion = fechaFinalizacion;
        }

        public String getTecnicoResponsable() {
            return tecnicoResponsable;
        }

        public void setTecnicoResponsable(String tecnicoResponsable) {
            this.tecnicoResponsable = tecnicoResponsable;
        }

        public String getObservacionesAnalisis() {
            return observacionesAnalisis;
        }

        public void setObservacionesAnalisis(String observacionesAnalisis) {
            this.observacionesAnalisis = observacionesAnalisis;
        }
    }

    // Getters and Setters principales
    public UUID getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(UUID idMuestra) {
        this.idMuestra = idMuestra;
    }

    public String getNumeroInterno() {
        return numeroInterno;
    }

    public void setNumeroInterno(String numeroInterno) {
        this.numeroInterno = numeroInterno;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public ClienteBasicDTO getCliente() {
        return cliente;
    }

    public void setCliente(ClienteBasicDTO cliente) {
        this.cliente = cliente;
    }

    public String getPuntoMuestreo() {
        return puntoMuestreo;
    }

    public void setPuntoMuestreo(String puntoMuestreo) {
        this.puntoMuestreo = puntoMuestreo;
    }

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaMuestreo() {
        return fechaMuestreo;
    }

    public void setFechaMuestreo(LocalDateTime fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
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

    public String getResponsableMuestreo() {
        return responsableMuestreo;
    }

    public void setResponsableMuestreo(String responsableMuestreo) {
        this.responsableMuestreo = responsableMuestreo;
    }

    public BigDecimal getTemperaturaTransporte() {
        return temperaturaTransporte;
    }

    public void setTemperaturaTransporte(BigDecimal temperaturaTransporte) {
        this.temperaturaTransporte = temperaturaTransporte;
    }

    public String getCondicionesTransporte() {
        return condicionesTransporte;
    }

    public void setCondicionesTransporte(String condicionesTransporte) {
        this.condicionesTransporte = condicionesTransporte;
    }

    public String getTipoEnvase() {
        return tipoEnvase;
    }

    public void setTipoEnvase(String tipoEnvase) {
        this.tipoEnvase = tipoEnvase;
    }

    public Boolean getConservantesUtilizados() {
        return conservantesUtilizados;
    }

    public void setConservantesUtilizados(Boolean conservantesUtilizados) {
        this.conservantesUtilizados = conservantesUtilizados;
    }

    public String getDescripcionConservantes() {
        return descripcionConservantes;
    }

    public void setDescripcionConservantes(String descripcionConservantes) {
        this.descripcionConservantes = descripcionConservantes;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getInfoCliente() {
        return infoCliente;
    }

    public void setInfoCliente(String infoCliente) {
        this.infoCliente = infoCliente;
    }

    public String getMetodoAnalisis() {
        return metodoAnalisis;
    }

    public void setMetodoAnalisis(String metodoAnalisis) {
        this.metodoAnalisis = metodoAnalisis;
    }

    public BigDecimal getVolumenMuestra() {
        return volumenMuestra;
    }

    public void setVolumenMuestra(BigDecimal volumenMuestra) {
        this.volumenMuestra = volumenMuestra;
    }

    public String getUnidadVolumen() {
        return unidadVolumen;
    }

    public void setUnidadVolumen(String unidadVolumen) {
        this.unidadVolumen = unidadVolumen;
    }

    public List<MuestraAnalisisDTO> getAnalisis() {
        return analisis;
    }

    public void setAnalisis(List<MuestraAnalisisDTO> analisis) {
        this.analisis = analisis;
    }

    public Integer getTotalAnalisis() {
        return totalAnalisis;
    }

    public void setTotalAnalisis(Integer totalAnalisis) {
        this.totalAnalisis = totalAnalisis;
    }

    public Integer getAnalisisCompletados() {
        return analisisCompletados;
    }

    public void setAnalisisCompletados(Integer analisisCompletados) {
        this.analisisCompletados = analisisCompletados;
    }

    public Double getProgresoPorcentaje() {
        return progresoPorcentaje;
    }

    public void setProgresoPorcentaje(Double progresoPorcentaje) {
        this.progresoPorcentaje = progresoPorcentaje;
    }

    @Override
    public String toString() {
        return "MuestraDTO{" +
                "idMuestra=" + idMuestra +
                ", numeroInterno='" + numeroInterno + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", puntoMuestreo='" + puntoMuestreo + '\'' +
                ", estado='" + estado + '\'' +
                ", totalAnalisis=" + totalAnalisis +
                '}';
    }
}