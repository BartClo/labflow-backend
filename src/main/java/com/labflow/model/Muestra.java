package com.labflow.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Entidad que representa una muestra de laboratorio
 * Incluye información completa de muestreo, transporte y análisis
 */
@Entity
@Table(name = "muestras")
public class Muestra {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_muestra")
    private UUID idMuestra;

    @Column(name = "numero_interno", nullable = true, length = 100, unique = true)
    @Size(max = 100, message = "El número interno no puede exceder 100 caracteres")
    private String numeroInterno;

    @Column(name = "codigo_barras", length = 100, unique = true, nullable = false)
    @NotBlank(message = "El código de barras es requerido")
    @Size(max = 100, message = "El código de barras no puede exceder 100 caracteres")
    private String codigoBarras;

    // Información del cliente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @NotNull(message = "El cliente es requerido")
    private Client cliente;

    @Column(name = "punto_muestreo", length = 255)
    @NotBlank(message = "El punto de muestreo es requerido")
    @Size(max = 255, message = "El punto de muestreo no puede exceder 255 caracteres")
    private String puntoMuestreo;

    // Información de la muestra
    @Column(name = "tipo_muestra", length = 100)
    @Size(max = 100, message = "El tipo de muestra no puede exceder 100 caracteres")
    private String tipoMuestra;

    @Enumerated(EnumType.STRING)
    @Column(name = "prioridad", length = 20)
    private Prioridad prioridad = Prioridad.MEDIA;

    // Condiciones de transporte
    @Column(name = "temperatura_transporte", precision = 5, scale = 2)
    @DecimalMin(value = "-50.0", message = "La temperatura de transporte debe ser mayor a -50°C")
    @DecimalMax(value = "100.0", message = "La temperatura de transporte debe ser menor a 100°C")
    private BigDecimal temperaturaTransporte;

    @Column(name = "tipo_contenedor", length = 100)
    @Size(max = 100, message = "El tipo de contenedor no puede exceder 100 caracteres")
    private String tipoContenedor;

    @Column(name = "metodo_preservacion", length = 255)
    @Size(max = 255, message = "El método de preservación no puede exceder 255 caracteres")
    private String metodoPreservacion;

    // Campos adicionales del frontend
    @Column(name = "responsable_muestreo", length = 255)
    @Size(max = 255, message = "El responsable del muestreo no puede exceder 255 caracteres")
    private String responsableMuestreo;

    @Column(name = "condiciones_transporte", length = 500)
    @Size(max = 500, message = "Las condiciones de transporte no pueden exceder 500 caracteres")
    private String condicionesTransporte;

    @Column(name = "tipo_envase", length = 100)
    @Size(max = 100, message = "El tipo de envase no puede exceder 100 caracteres")
    private String tipoEnvase;

    @Column(name = "conservantes_utilizados")
    private Boolean conservantesUtilizados = false;

    @Column(name = "descripcion_conservantes", length = 255)
    @Size(max = 255, message = "La descripción de conservantes no puede exceder 255 caracteres")
    private String descripcionConservantes;

    @Column(name = "info_cliente", length = 255)
    @Size(max = 255, message = "La información del cliente no puede exceder 255 caracteres")
    private String infoCliente;

    @Column(name = "metodo_analisis", length = 255)
    @Size(max = 255, message = "El método de análisis no puede exceder 255 caracteres")
    private String metodoAnalisis;

    @Column(name = "volumen_muestra", precision = 10, scale = 3)
    @DecimalMin(value = "0.0", message = "El volumen debe ser positivo")
    private BigDecimal volumenMuestra;

    @Column(name = "unidad_volumen", length = 50)
    @Size(max = 50, message = "La unidad de volumen no puede exceder 50 caracteres")
    private String unidadVolumen;

    // Fechas y recepción
    @Column(name = "fecha_recepcion")
    private LocalDateTime fechaRecepcion;

    @Column(name = "fecha_muestreo")
    private LocalDateTime fechaMuestreo;

    @Column(name = "recibida_por", length = 255)
    @Size(max = 255, message = "Recibida por no puede exceder 255 caracteres")
    private String recibidaPor;

    // Estado de la muestra
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 50)
    private EstadoMuestra estado = EstadoMuestra.RECIBIDA;

    // Observaciones
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    // Metadatos
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    // Relaciones con análisis
    @OneToMany(mappedBy = "muestra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<MuestraAnalisis> muestraAnalisis;

    // Relaciones con plantillas
    @OneToMany(mappedBy = "muestra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<MuestraPlantilla> muestraPlantillas;

    // Enums
    public enum Prioridad {
        BAJA, MEDIA, ALTA
    }

    public enum EstadoMuestra {
        RECIBIDA, EN_PROCESO, ANALIZADA, COMPLETADA, RECHAZADA
    }

    // Constructors
    public Muestra() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
    }

    // Lifecycle callbacks
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        if (this.fechaCreacion == null) {
            this.fechaCreacion = LocalDateTime.now();
        }
        if (this.fechaActualizacion == null) {
            this.fechaActualizacion = LocalDateTime.now();
        }
    }

    // Getters and Setters
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

    public Client getCliente() {
        return cliente;
    }

    public void setCliente(Client cliente) {
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

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public BigDecimal getTemperaturaTransporte() {
        return temperaturaTransporte;
    }

    public void setTemperaturaTransporte(BigDecimal temperaturaTransporte) {
        this.temperaturaTransporte = temperaturaTransporte;
    }

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public String getMetodoPreservacion() {
        return metodoPreservacion;
    }

    public void setMetodoPreservacion(String metodoPreservacion) {
        this.metodoPreservacion = metodoPreservacion;
    }

    public String getResponsableMuestreo() {
        return responsableMuestreo;
    }

    public void setResponsableMuestreo(String responsableMuestreo) {
        this.responsableMuestreo = responsableMuestreo;
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

    public LocalDateTime getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setFechaRecepcion(LocalDateTime fechaRecepcion) {
        this.fechaRecepcion = fechaRecepcion;
    }

    public LocalDateTime getFechaMuestreo() {
        return fechaMuestreo;
    }

    public void setFechaMuestreo(LocalDateTime fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public String getRecibidaPor() {
        return recibidaPor;
    }

    public void setRecibidaPor(String recibidaPor) {
        this.recibidaPor = recibidaPor;
    }

    public EstadoMuestra getEstado() {
        return estado;
    }

    public void setEstado(EstadoMuestra estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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

    public Set<MuestraAnalisis> getMuestraAnalisis() {
        return muestraAnalisis;
    }

    public void setMuestraAnalisis(Set<MuestraAnalisis> muestraAnalisis) {
        this.muestraAnalisis = muestraAnalisis;
    }

    public Set<MuestraPlantilla> getMuestraPlantillas() {
        return muestraPlantillas;
    }

    public void setMuestraPlantillas(Set<MuestraPlantilla> muestraPlantillas) {
        this.muestraPlantillas = muestraPlantillas;
    }

    // Métodos de utilidad
    public int getCantidadAnalisis() {
        return muestraAnalisis != null ? muestraAnalisis.size() : 0;
    }

    public int getCantidadPlantillas() {
        return muestraPlantillas != null ? muestraPlantillas.size() : 0;
    }

    public boolean tieneAnalisisPendientes() {
        if (muestraAnalisis == null) return false;
        return muestraAnalisis.stream()
                .anyMatch(ma -> ma.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE 
                            || ma.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.EN_PROCESO);
    }

    public boolean tienePlantillasPendientes() {
        if (muestraPlantillas == null) return false;
        return muestraPlantillas.stream()
                .anyMatch(mp -> mp.getEstadoPlantilla() == MuestraPlantilla.EstadoPlantilla.PENDIENTE 
                            || mp.getEstadoPlantilla() == MuestraPlantilla.EstadoPlantilla.EN_PROCESO);
    }

    public boolean tieneTrabajosPendientes() {
        return tieneAnalisisPendientes() || tienePlantillasPendientes();
    }

    @Override
    public String toString() {
        return "Muestra{" +
                "idMuestra=" + idMuestra +
                ", numeroInterno='" + numeroInterno + '\'' +
                ", tipoMuestra='" + tipoMuestra + '\'' +
                ", estado=" + estado +
                ", prioridad=" + prioridad +
                '}';
    }
}