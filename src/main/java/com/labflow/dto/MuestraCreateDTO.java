package com.labflow.dto;

import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO para la creación de nuevas muestras
 * Mapea directamente con los campos del frontend
 */
public class MuestraCreateDTO {

    @Size(max = 100, message = "El número interno no puede exceder 100 caracteres")
    @JsonProperty("numeroInterno")
    private String numeroInterno;

    @NotBlank(message = "El código de barras es requerido")
    @Size(max = 100, message = "El código de barras no puede exceder 100 caracteres")
    @JsonProperty("codigoBarras")
    private String codigoBarras;

    @NotNull(message = "El cliente es requerido")
    @JsonProperty("idCliente")
    private UUID idCliente;

    @NotBlank(message = "El punto de muestreo es requerido")
    @Size(max = 255, message = "El punto de muestreo no puede exceder 255 caracteres")
    @JsonProperty("puntoMuestreo")
    private String puntoMuestreo;

    @Size(max = 100, message = "El tipo de muestra no puede exceder 100 caracteres")
    @JsonProperty("tipoMuestra")
    private String tipoMuestra;

    @Pattern(regexp = "(?i)(alta|media|baja)", message = "La prioridad debe ser alta, media o baja")
    @Schema(description = "Prioridad de la muestra", example = "media")
    @JsonProperty("prioridad")
    private String prioridad = "media";

    @JsonProperty("fechaMuestreo")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
    @Schema(description = "Fecha y hora de muestreo en formato ISO-8601", example = "2025-01-15T14:30:00Z")
    private LocalDateTime fechaMuestreo;

    @JsonProperty("fechaRecepcion")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[XXX]")
    @Schema(description = "Fecha y hora de recepción en formato ISO-8601", example = "2025-01-15T16:00:00Z")
    private LocalDateTime fechaRecepcion;

    @Size(max = 255, message = "El responsable del muestreo no puede exceder 255 caracteres")
    @JsonProperty("responsableMuestreo")
    private String responsableMuestreo;

    // Condiciones de transporte
    @DecimalMin(value = "-50.0", message = "La temperatura de transporte debe ser mayor a -50°C")
    @DecimalMax(value = "100.0", message = "La temperatura de transporte debe ser menor a 100°C")
    @JsonProperty("temperaturaTransporte")
    private BigDecimal temperaturaTransporte;

    @Size(max = 500, message = "Las condiciones de transporte no pueden exceder 500 caracteres")
    @JsonProperty("condicionesTransporte")
    private String condicionesTransporte;

    @Size(max = 100, message = "El tipo de envase no puede exceder 100 caracteres")
    @JsonProperty("tipoEnvase")
    private String tipoEnvase;

    @JsonProperty("conservantesUtilizados")
    private Boolean conservantesUtilizados = false;

    @Size(max = 255, message = "La descripción de conservantes no puede exceder 255 caracteres")
    @JsonProperty("descripcionConservantes")
    private String descripcionConservantes;

    // Observaciones y estado
    @Size(max = 1000, message = "Las observaciones no pueden exceder 1000 caracteres")
    @JsonProperty("observaciones")
    private String observaciones;

    @Pattern(regexp = "(?i)(recibida|en_proceso|analizada|completada|rechazada)", 
             message = "El estado debe ser recibida, en_proceso, analizada, completada o rechazada")
    @Schema(description = "Estado inicial de la muestra", example = "recibida")
    @JsonProperty("estado")
    private String estado = "recibida";

    // Análisis a realizar
    @JsonProperty("analisisIds")
    private List<UUID> analisisIds;

    // Plantillas a aplicar
    @JsonProperty("plantillaIds")
    private List<UUID> plantillaIds;

    // Datos adicionales del frontend
    @Size(max = 255, message = "La información del cliente no puede exceder 255 caracteres")
    @JsonProperty("infoCliente")
    private String infoCliente;

    @Size(max = 255, message = "El método de análisis no puede exceder 255 caracteres")
    @JsonProperty("metodoAnalisis")
    private String metodoAnalisis;

    @JsonProperty("volumenMuestra")
    @DecimalMin(value = "0.0", message = "El volumen debe ser positivo")
    private BigDecimal volumenMuestra;

    @Size(max = 50, message = "La unidad de volumen no puede exceder 50 caracteres")
    @JsonProperty("unidadVolumen")
    private String unidadVolumen;

    // Campos adicionales del frontend
    @Size(max = 255, message = "El nombre del proyecto no puede exceder 255 caracteres")
    @JsonProperty("nombreProyecto")
    private String nombreProyecto;

    @Size(max = 100, message = "El número de solicitud no puede exceder 100 caracteres")
    @JsonProperty("numeroSolicitud")
    private String numeroSolicitud;

    @Size(max = 100, message = "La condición de la muestra no puede exceder 100 caracteres")
    @JsonProperty("condicionMuestra")
    private String condicionMuestra;

    @Size(max = 50, message = "La unidad de temperatura no puede exceder 50 caracteres")
    @JsonProperty("unidadTemperatura")
    private String unidadTemperatura;

    // Constructor por defecto requerido para deserialización JSON
    public MuestraCreateDTO() {
        // Constructor vacío necesario para Jackson JSON binding
    }

    // Getters and Setters
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

    public UUID getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(UUID idCliente) {
        this.idCliente = idCliente;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public List<UUID> getAnalisisIds() {
        return analisisIds;
    }

    public void setAnalisisIds(List<UUID> analisisIds) {
        this.analisisIds = analisisIds;
    }

    public List<UUID> getPlantillaIds() {
        return plantillaIds;
    }

    public void setPlantillaIds(List<UUID> plantillaIds) {
        this.plantillaIds = plantillaIds;
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

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public String getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(String numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getCondicionMuestra() {
        return condicionMuestra;
    }

    public void setCondicionMuestra(String condicionMuestra) {
        this.condicionMuestra = condicionMuestra;
    }

    public String getUnidadTemperatura() {
        return unidadTemperatura;
    }

    public void setUnidadTemperatura(String unidadTemperatura) {
        this.unidadTemperatura = unidadTemperatura;
    }

    @Override
    public String toString() {
        return "MuestraCreateDTO{" +
                "numeroInterno='" + numeroInterno + '\'' +
                ", codigoBarras='" + codigoBarras + '\'' +
                ", idCliente=" + idCliente +
                ", puntoMuestreo='" + puntoMuestreo + '\'' +
                ", tipoMuestra='" + tipoMuestra + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", estado='" + estado + '\'' +
                ", analisisIds=" + analisisIds +
                ", plantillaIds=" + plantillaIds +
                '}';
    }

    /**
     * Validación personalizada para asegurar que se especifique al menos análisis o plantillas
     */
    public boolean tieneAnalisisOPlantillas() {
        boolean tieneAnalisis = analisisIds != null && !analisisIds.isEmpty();
        boolean tienePlantillas = plantillaIds != null && !plantillaIds.isEmpty();
        return tieneAnalisis || tienePlantillas;
    }

    public boolean soloTieneAnalisis() {
        boolean tieneAnalisis = analisisIds != null && !analisisIds.isEmpty();
        boolean tienePlantillas = plantillaIds != null && !plantillaIds.isEmpty();
        return tieneAnalisis && !tienePlantillas;
    }

    public boolean soloTienePlantillas() {
        boolean tieneAnalisis = analisisIds != null && !analisisIds.isEmpty();
        boolean tienePlantillas = plantillaIds != null && !plantillaIds.isEmpty();
        return !tieneAnalisis && tienePlantillas;
    }

    public boolean tieneMixto() {
        boolean tieneAnalisis = analisisIds != null && !analisisIds.isEmpty();
        boolean tienePlantillas = plantillaIds != null && !plantillaIds.isEmpty();
        return tieneAnalisis && tienePlantillas;
    }
}