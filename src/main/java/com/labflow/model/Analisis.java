package com.labflow.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Entidad Analisis que representa un análisis de laboratorio en el sistema LabFlow
 */
@Entity
@Table(name = "analisis")
public class Analisis {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_analisis", updatable = false, nullable = false)
    private UUID idAnalisis;

    @Column(name = "nombre_analisis", nullable = false, length = 255)
    private String nombreAnalisis;

    @Column(name = "codigo", nullable = false, length = 50, unique = true)
    private String codigo;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Column(name = "metodo_ensayo", length = 255)
    private String metodoEnsayo;

    @Column(name = "tipos_muestra_aplicables", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> tiposMuestraAplicables;

    @Column(name = "parametros_medir", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> parametrosMedir;

    @Column(name = "equipos_requeridos", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> equiposRequeridos;

    @Column(name = "insumos_requeridos", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> insumosRequeridos;

    @Column(name = "reactivos_requeridos", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> reactivosRequeridos;

    @Column(name = "duracion_estimada_horas", precision = 5, scale = 2)
    private BigDecimal duracionEstimadaHoras;

    @Column(name = "precio_clp", precision = 10, scale = 2)
    private BigDecimal precioClp;

    @Column(name = "dias_entrega")
    private Integer diasEntrega;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "Activo";

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Constructores
    public Analisis() {
    }

    public Analisis(String nombreAnalisis, String codigo, String categoria) {
        this.nombreAnalisis = nombreAnalisis;
        this.codigo = codigo;
        this.categoria = categoria;
    }

    // Getters y Setters
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMetodoEnsayo() {
        return metodoEnsayo;
    }

    public void setMetodoEnsayo(String metodoEnsayo) {
        this.metodoEnsayo = metodoEnsayo;
    }

    public List<String> getTiposMuestraAplicables() {
        return tiposMuestraAplicables;
    }

    public void setTiposMuestraAplicables(List<String> tiposMuestraAplicables) {
        this.tiposMuestraAplicables = tiposMuestraAplicables;
    }

    public Map<String, Object> getParametrosMedir() {
        return parametrosMedir;
    }

    public void setParametrosMedir(Map<String, Object> parametrosMedir) {
        this.parametrosMedir = parametrosMedir;
    }

    public Map<String, Object> getEquiposRequeridos() {
        return equiposRequeridos;
    }

    public void setEquiposRequeridos(Map<String, Object> equiposRequeridos) {
        this.equiposRequeridos = equiposRequeridos;
    }

    public Map<String, Object> getInsumosRequeridos() {
        return insumosRequeridos;
    }

    public void setInsumosRequeridos(Map<String, Object> insumosRequeridos) {
        this.insumosRequeridos = insumosRequeridos;
    }

    public Map<String, Object> getReactivosRequeridos() {
        return reactivosRequeridos;
    }

    public void setReactivosRequeridos(Map<String, Object> reactivosRequeridos) {
        this.reactivosRequeridos = reactivosRequeridos;
    }

    public BigDecimal getDuracionEstimadaHoras() {
        return duracionEstimadaHoras;
    }

    public void setDuracionEstimadaHoras(BigDecimal duracionEstimadaHoras) {
        this.duracionEstimadaHoras = duracionEstimadaHoras;
    }

    public BigDecimal getPrecioClp() {
        return precioClp;
    }

    public void setPrecioClp(BigDecimal precioClp) {
        this.precioClp = precioClp;
    }

    public Integer getDiasEntrega() {
        return diasEntrega;
    }

    public void setDiasEntrega(Integer diasEntrega) {
        this.diasEntrega = diasEntrega;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
        return "Analisis{" +
                "idAnalisis=" + idAnalisis +
                ", nombreAnalisis='" + nombreAnalisis + '\'' +
                ", codigo='" + codigo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", estado='" + estado + '\'' +
                ", precioClp=" + precioClp +
                '}';
    }

    // equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Analisis analisis = (Analisis) o;
        return idAnalisis != null && idAnalisis.equals(analisis.idAnalisis);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}