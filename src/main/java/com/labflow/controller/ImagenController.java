package com.labflow.controller;

import com.labflow.dto.ImagenDTO;
import com.labflow.model.Imagen;
import com.labflow.service.ImagenService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/muestras/{muestraId}/imagen")
public class ImagenController {

    private final ImagenService imagenService;

    public ImagenController(ImagenService imagenService) {
        this.imagenService = imagenService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImagenDTO> subirArchivo(
            @PathVariable UUID muestraId,
            @RequestParam("archivo") MultipartFile archivo) {

        ImagenDTO imagen = imagenService.guardarImagenMuestra(muestraId, archivo);
        return ResponseEntity.ok(imagen);
    }

    @GetMapping
    public ResponseEntity<byte[]> descargarArchivo(@PathVariable UUID muestraId) {
        Imagen imagen = imagenService.obtenerArchivo(muestraId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(imagen.getMimeType()));
        headers.setContentLength(imagen.getTamanoBytes());
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename(imagen.getNombreArchivo())
                .build());

        return ResponseEntity.ok()
                .headers(headers)
                .body(imagen.getContenido());
    }

    @GetMapping("/metadatos")
    public ResponseEntity<ImagenDTO> obtenerMetadatos(@PathVariable UUID muestraId) {
        ImagenDTO imagen = imagenService.obtenerMetadatos(muestraId);
        return ResponseEntity.ok(imagen);
    }

    @DeleteMapping
    public ResponseEntity<Void> eliminarArchivo(@PathVariable UUID muestraId) {
        imagenService.eliminarArchivo(muestraId);
        return ResponseEntity.noContent().build();
    }
}
