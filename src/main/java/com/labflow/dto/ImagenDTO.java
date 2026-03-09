package com.labflow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public class ImagenDTO {

    @JsonProperty("id_imagen")
    private UUID idImagen;

    @JsonProperty("id_muestra")
    private UUID idMuestra;

    @JsonProperty("mime_type")
    private String mimeType;

    @JsonProperty("nombre_archivo")
    private String nombreArchivo;

    @JsonProperty("tamano_bytes")
    private Long tamanoBytes;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

    public UUID getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(UUID idImagen) {
        this.idImagen = idImagen;
    }

    public UUID getIdMuestra() {
        return idMuestra;
    }

    public void setIdMuestra(UUID idMuestra) {
        this.idMuestra = idMuestra;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Long getTamanoBytes() {
        return tamanoBytes;
    }

    public void setTamanoBytes(Long tamanoBytes) {
        this.tamanoBytes = tamanoBytes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
