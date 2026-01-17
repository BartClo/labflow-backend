package com.labflow.service;

import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.*;
import com.labflow.repository.MuestraAnalisisRepository;
import com.labflow.repository.OrdenTrabajoRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Servicio para generar informes y certificados PDF
 */
@Service
@Transactional(readOnly = true)
public class InformeService {

    private static final Logger logger = LoggerFactory.getLogger(InformeService.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @Autowired
    private MuestraAnalisisRepository muestraAnalisisRepository;

    /**
     * Genera el certificado PDF oficial de resultados para una orden de trabajo
     */
    public byte[] generarPdfCertificado(UUID ordenTrabajoId) {
        logger.info("Generando certificado PDF para orden de trabajo: {}", ordenTrabajoId);

        // Buscar la orden de trabajo
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + ordenTrabajoId));

        // Obtener todas las tareas de la orden
        List<MuestraAnalisis> tareas = muestraAnalisisRepository.findByOrdenTrabajo(ordenTrabajo);

        if (tareas.isEmpty()) {
            throw new ValidationException("La orden de trabajo no tiene tareas asignadas");
        }

        // VALIDACIÓN DE BLOQUEO: Verificar que no haya tareas pendientes
        long tareasPendientes = tareas.stream()
                .filter(t -> t.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE)
                .count();

        if (tareasPendientes > 0) {
            throw new ValidationException(
                    String.format("No se puede generar el PDF. Hay %d tarea(s) pendiente(s) en esta orden de trabajo. " +
                            "Todas las tareas deben estar completadas antes de generar el certificado.",
                            tareasPendientes));
        }

        try {
            // Obtener datos del cliente de la primera muestra
            Muestra primeraMustra = tareas.get(0).getMuestra();
            Client cliente = primeraMustra.getCliente();

            // Preparar parámetros del reporte
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("ordenTrabajoId", ordenTrabajo.getId().toString().substring(0, 8).toUpperCase());
            parameters.put("fechaEmision", java.time.LocalDateTime.now().format(DATE_FORMATTER));
            parameters.put("tecnicoNombre", ordenTrabajo.getTecnicoAsignado().getNombre() + " " + 
                    ordenTrabajo.getTecnicoAsignado().getApellido());
            parameters.put("clienteNombre", cliente.getEmpresa() != null ? 
                    cliente.getEmpresa() : cliente.getNombreCliente());
            parameters.put("clienteDireccion", cliente.getDireccion());
            parameters.put("clienteRut", "");
            parameters.put("numeroMuestra", primeraMustra.getNumeroInterno());

            // Path del logo (placeholder por ahora)
            parameters.put("logoPath", "");

            // Extraer parámetros de resultados de todas las tareas
            List<Map<String, Object>> resultadosData = new ArrayList<>();

            for (MuestraAnalisis tarea : tareas) {
                if (tarea.getResultado() != null && tarea.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.COMPLETADO) {
                    List<Map<String, Object>> parametrosExtraidos = extraerParametrosDeResultado(tarea.getResultado());
                    resultadosData.addAll(parametrosExtraidos);
                }
            }

            if (resultadosData.isEmpty()) {
                throw new ValidationException("No hay resultados disponibles para generar el certificado");
            }

            // Crear DataSource para JasperReports
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(resultadosData);

            // Cargar el template JRXML
            InputStream reportStream = new ClassPathResource("reports/certificado-resultados.jrxml").getInputStream();

            // Compilar el reporte
            JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

            // Llenar el reporte con datos
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            // Exportar a PDF
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            logger.info("Certificado PDF generado exitosamente para orden {} ({} bytes)", 
                    ordenTrabajoId, pdfBytes.length);

            return pdfBytes;

        } catch (Exception e) {
            logger.error("Error al generar PDF para orden {}: {}", ordenTrabajoId, e.getMessage(), e);
            throw new RuntimeException("Error al generar el certificado PDF: " + e.getMessage(), e);
        }
    }

    /**
     * Extrae los parámetros del resultado JSONB para el reporte
     */
    private List<Map<String, Object>> extraerParametrosDeResultado(Map<String, Object> resultado) {
        List<Map<String, Object>> parametros = new ArrayList<>();

        try {
            // Extraer la sección de parámetros del JSONB
            Object parametrosObj = resultado.get("parametros");

            if (parametrosObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Map<String, Object>> parametrosMap = (Map<String, Map<String, Object>>) parametrosObj;

                for (Map.Entry<String, Map<String, Object>> entry : parametrosMap.entrySet()) {
                    String nombreParametro = entry.getKey();
                    Map<String, Object> paramData = entry.getValue();

                    // Crear fila para el reporte
                    Map<String, Object> row = new HashMap<>();
                    row.put("parametro", nombreParametro);
                    row.put("valor", paramData.get("valor") != null ? paramData.get("valor").toString() : "N/A");
                    row.put("unidad", paramData.get("unidad") != null ? paramData.get("unidad").toString() : "");
                    row.put("limiteNormativo", paramData.get("valor_maximo_normativa") != null ? 
                            paramData.get("valor_maximo_normativa").toString() : "N/A");
                    row.put("cumpleNormativa", paramData.get("cumple_normativa") != null ? 
                            (Boolean) paramData.get("cumple_normativa") : true);

                    parametros.add(row);
                }
            }

        } catch (Exception e) {
            logger.error("Error al extraer parámetros del resultado: {}", e.getMessage(), e);
        }

        return parametros;
    }

    /**
     * Valida si una orden de trabajo está lista para generar el certificado
     */
    public boolean estaListaParaCertificado(UUID ordenTrabajoId) {
        // Verificar que la orden existe
        ordenTrabajoRepository.findById(ordenTrabajoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Orden de trabajo no encontrada con ID: " + ordenTrabajoId));

        Long tareasPendientes = muestraAnalisisRepository.countTareasPendientesByOrdenTrabajo(ordenTrabajoId);

        return tareasPendientes == 0;
    }
}
