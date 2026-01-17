package com.labflow.controller;

import com.labflow.service.InformeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controlador REST para gestionar Informes y Certificados
 */
@RestController
@RequestMapping("/api/informes")
public class InformeController {

    private static final Logger logger = LoggerFactory.getLogger(InformeController.class);

    @Autowired
    private InformeService informeService;

    /**
     * Genera y descarga el certificado PDF oficial de resultados
     * GET /api/informes/pdf/{ot_id}
     */
    @GetMapping("/pdf/{ot_id}")
    public ResponseEntity<byte[]> generarCertificadoPdf(@PathVariable("ot_id") UUID otId) {
        logger.info("Solicitud de generación de certificado PDF para orden: {}", otId);

        try {
            // Generar el PDF
            byte[] pdfBytes = informeService.generarPdfCertificado(otId);

            // Configurar headers para descarga del PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", 
                    "certificado-" + otId.toString().substring(0, 8) + ".pdf");
            headers.setContentLength(pdfBytes.length);

            logger.info("Certificado PDF generado exitosamente para orden {}", otId);

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            logger.error("Error al generar certificado PDF para orden {}: {}", otId, e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si una orden de trabajo está lista para generar el certificado
     * GET /api/informes/pdf/{ot_id}/validar
     */
    @GetMapping("/pdf/{ot_id}/validar")
    public ResponseEntity<Map<String, Object>> validarOrdenParaCertificado(
            @PathVariable("ot_id") UUID otId) {

        logger.info("Validando si orden {} está lista para certificado", otId);

        boolean estaLista = informeService.estaListaParaCertificado(otId);

        Map<String, Object> response = new HashMap<>();
        response.put("orden_trabajo_id", otId);
        response.put("puede_generar_certificado", estaLista);
        response.put("mensaje", estaLista ? 
                "La orden está lista para generar el certificado" : 
                "La orden tiene tareas pendientes, no se puede generar el certificado");

        return ResponseEntity.ok(response);
    }
}
