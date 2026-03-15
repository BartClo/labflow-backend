package com.labflow.service;

import com.labflow.dto.ImagenDTO;
import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.Imagen;
import com.labflow.model.Muestra;
import com.labflow.repository.ImagenRepository;
import com.labflow.repository.MuestraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class ImagenService {

    private final ImagenRepository imagenRepository;
    private final MuestraRepository muestraRepository;

    public ImagenService(ImagenRepository imagenRepository, MuestraRepository muestraRepository) {
        this.imagenRepository = imagenRepository;
        this.muestraRepository = muestraRepository;
    }

    public ImagenDTO guardarImagenMuestra(UUID muestraId, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new ValidationException("Debe enviar un archivo");
        }

        String mimeType = archivo.getContentType();
        validarMimeType(mimeType);

        Muestra muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException("Muestra no encontrada con ID: " + muestraId));

        Imagen imagen = imagenRepository.findByMuestraIdMuestra(muestraId).orElseGet(Imagen::new);
        imagen.setMuestra(muestra);
        imagen.setMimeType(mimeType);
        imagen.setNombreArchivo(obtenerNombreSeguro(archivo.getOriginalFilename()));
        imagen.setTamanoBytes(archivo.getSize());

        try {
            imagen.setContenido(archivo.getBytes());
        } catch (IOException e) {
            throw new ValidationException("No se pudo leer el archivo enviado", e);
        }

        imagen = imagenRepository.save(imagen);
        return convertirADto(imagen);
    }

    @Transactional(readOnly = true)
    public Imagen obtenerArchivo(UUID muestraId) {
        return imagenRepository.findByMuestraIdMuestra(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un archivo asociado a la muestra: " + muestraId));
    }

    @Transactional(readOnly = true)
    public ImagenDTO obtenerMetadatos(UUID muestraId) {
        Imagen imagen = obtenerArchivo(muestraId);
        return convertirADto(imagen);
    }

    public void eliminarArchivo(UUID muestraId) {
        if (!imagenRepository.existsByMuestraIdMuestra(muestraId)) {
            throw new ResourceNotFoundException("No existe un archivo asociado a la muestra: " + muestraId);
        }

        imagenRepository.deleteByMuestraIdMuestra(muestraId);
    }

    private void validarMimeType(String mimeType) {
        if (mimeType == null || mimeType.isBlank()) {
            throw new ValidationException("No se pudo determinar el tipo del archivo");
        }

        String mimeTypeNormalizado = mimeType.toLowerCase(Locale.ROOT);
        boolean esPdf = "application/pdf".equals(mimeTypeNormalizado);
        boolean esImagen = mimeTypeNormalizado.startsWith("image/");

        if (!esPdf && !esImagen) {
            throw new ValidationException("Solo se permiten archivos de tipo imagen o PDF");
        }
    }

    private String obtenerNombreSeguro(String nombreOriginal) {
        if (nombreOriginal == null || nombreOriginal.isBlank()) {
            return "archivo";
        }
        return nombreOriginal;
    }

    private ImagenDTO convertirADto(Imagen imagen) {
        ImagenDTO dto = new ImagenDTO();
        dto.setIdImagen(imagen.getIdImagen());
        dto.setIdMuestra(imagen.getMuestra().getIdMuestra());
        dto.setMimeType(imagen.getMimeType());
        dto.setNombreArchivo(imagen.getNombreArchivo());
        dto.setTamanoBytes(imagen.getTamanoBytes());
        dto.setCreatedAt(imagen.getCreatedAt());
        dto.setUpdatedAt(imagen.getUpdatedAt());
        return dto;
    }
}
