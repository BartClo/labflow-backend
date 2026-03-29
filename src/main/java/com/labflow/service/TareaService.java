package com.labflow.service;

import com.labflow.dto.TareaPendienteDTO;
import com.labflow.dto.request.GuardarResultadoSimpleDTO;
import com.labflow.dto.request.ResultadoUpdateDTO;
import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.*;
import com.labflow.repository.MuestraAnalisisRepository;
import com.labflow.repository.ParametroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar tareas (MuestraAnalisis)
 * Incluye validación automática NCh 409
 */
@Service
@Transactional
public class TareaService {

    private static final Logger logger = LoggerFactory.getLogger(TareaService.class);

    @Autowired
    private MuestraAnalisisRepository muestraAnalisisRepository;

    @Autowired
    private ParametroRepository parametroRepository;

    /**
     * Obtiene tareas pendientes sin orden de trabajo asignada
     */
    @Transactional(readOnly = true)
    public List<TareaPendienteDTO> obtenerTareasPendientes(UUID analisisId) {
        List<MuestraAnalisis> tareas;

        if (analisisId != null) {
            logger.info("Obteniendo tareas pendientes para análisis: {}", analisisId);
            tareas = muestraAnalisisRepository.findTareasPendientesSinOrdenByAnalisis(analisisId);
        } else {
            logger.info("Obteniendo todas las tareas pendientes");
            tareas = muestraAnalisisRepository.findTareasPendientesSinOrden();
        }

        return tareas.stream()
                .map(this::convertirATareaPendienteDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los resultados de una tarea y aplica validación NCh 409
     */
    public Map<String, Object> actualizarResultado(UUID tareaId, ResultadoUpdateDTO dto) {
        logger.info("Actualizando resultado para tarea: {}", tareaId);

        // Buscar la tarea
        MuestraAnalisis tarea = muestraAnalisisRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarea no encontrada con ID: " + tareaId));

        // Obtener los parámetros del análisis
        UUID analisisId = tarea.getAnalisis().getIdAnalisis();
        List<Parametro> parametrosDelAnalisis = parametroRepository.findByAnalisisId(analisisId);

        // Crear mapa de parámetros por nombre para búsqueda rápida
        Map<String, Parametro> parametrosMap = parametrosDelAnalisis.stream()
                .collect(Collectors.toMap(Parametro::getNombre, p -> p));

        // Construir resultado JSONB con validación NCh 409
        Map<String, Object> resultadoJson = new HashMap<>();
        Map<String, Map<String, Object>> parametrosResultado = new HashMap<>();
        boolean todoCumpleNormativa = true;
        List<String> parametrosNoCumplen = new ArrayList<>();

        // Procesar cada parámetro del DTO
        for (Map.Entry<String, ResultadoUpdateDTO.ParametroResultadoDTO> entry : dto.getParametros().entrySet()) {
            String nombreParametro = entry.getKey();
            ResultadoUpdateDTO.ParametroResultadoDTO paramDto = entry.getValue();

            // Buscar el parámetro en la base de datos
            Parametro parametro = parametrosMap.get(nombreParametro);
            
            Map<String, Object> paramResultado = new HashMap<>();
            
            if (parametro != null) {
                // Validar contra NCh 409 (Caso con parametro configurado)
                boolean cumpleNormativa = validarParametroContraNormativa(
                        paramDto.getValor(),
                        parametro.getValorMaximoNormativa()
                );

                if (!cumpleNormativa) {
                    todoCumpleNormativa = false;
                    parametrosNoCumplen.add(nombreParametro);
                }
                
                paramResultado.put("valor", paramDto.getValor());
                paramResultado.put("unidad", paramDto.getUnidad() != null ? paramDto.getUnidad() : parametro.getUnidad());
                paramResultado.put("cumple_normativa", cumpleNormativa);
                paramResultado.put("valor_maximo_normativa", parametro.getValorMaximoNormativa());
                if (paramDto.getObservaciones() != null) {
                    paramResultado.put("observaciones", paramDto.getObservaciones());
                }
                
                parametrosResultado.put(nombreParametro, paramResultado);

            } else if (parametrosMap.isEmpty()) {
                // Caso AD-HOC: Análisis sin parámetros configurados
                // Se guarda el valor tal cual, asumiendo cumplimiento normativo (o no aplicable)
                // y usando la unidad proporcionada si existe
                
                paramResultado.put("valor", paramDto.getValor());
                paramResultado.put("unidad", paramDto.getUnidad());
                paramResultado.put("cumple_normativa", true); // Asumimos OK 
                paramResultado.put("valor_maximo_normativa", null);
                if (paramDto.getObservaciones() != null) {
                    paramResultado.put("observaciones", paramDto.getObservaciones());
                }
                
                parametrosResultado.put(nombreParametro, paramResultado);
            } else {
                logger.warn("Parámetro '{}' no encontrado en el análisis (y existen otros configurados)", nombreParametro);
                continue;
            }
        }

        resultadoJson.put("parametros", parametrosResultado);

        // Agregar controles de calidad si fueron proporcionados
        if (dto.getControlesCalidad() != null) {
            resultadoJson.put("controles_calidad", dto.getControlesCalidad());
        }

        // Agregar metadata si fue proporcionada
        if (dto.getMetadata() != null) {
            resultadoJson.put("metadata", dto.getMetadata());
        } else {
            // Agregar metadata básica
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("fecha_analisis", java.time.LocalDateTime.now().toString());
            metadata.put("version_metodo", "NCh409/1:2005");
            resultadoJson.put("metadata", metadata);
        }

        // Actualizar la tarea con los resultados
        tarea.setResultado(resultadoJson);
        tarea.setCumpleNormativa(todoCumpleNormativa);
        tarea.setCumpleNorma(todoCumpleNormativa);  // Nuevo campo para workflow pull

        // Generar notas de validación si hay parámetros que no cumplen
        if (!todoCumpleNormativa) {
            String notas = String.format("Parámetros fuera de normativa NCh 409: %s",
                    String.join(", ", parametrosNoCumplen));
            tarea.setNotasValidacion(notas);
            logger.warn("Tarea {} - {}", tareaId, notas);
        } else {
            tarea.setNotasValidacion("Todos los parámetros cumplen con la normativa NCh 409");
            logger.info("Tarea {} - Todos los parámetros cumplen normativa", tareaId);
        }

        // Si todos los resultados están completos, completar el análisis
        if (dto.getParametros().size() >= parametrosDelAnalisis.size()) {
            tarea.completarAnalisis(resultadoJson);
            logger.info("Tarea {} completada con {} parámetros", tareaId, dto.getParametros().size());
        }

        // Guardar la tarea
        muestraAnalisisRepository.save(tarea);

        // Retornar información de la validación
        Map<String, Object> response = new HashMap<>();
        response.put("cumple_normativa", todoCumpleNormativa);
        response.put("parametros_validados", dto.getParametros().size());
        response.put("parametros_no_cumplen", parametrosNoCumplen);
        response.put("estado", tarea.getEstadoAnalisis().name());

        return response;
    }

    /**
     * Valida un parámetro contra la normativa NCh 409
     * Compara el valor ingresado vs el límite máximo normativo
     */
    private boolean validarParametroContraNormativa(String valorIngresado, String limiteNormativa) {
        if (valorIngresado == null || limiteNormativa == null) {
            logger.warn("Valor o límite normativo es null, no se puede validar");
            return true; // No se puede validar, asumir cumplimiento
        }

        try {
            // Manejar casos especiales
            String valorLower = valorIngresado.trim().toLowerCase();

            // Caso: "Ausencia" o "No Detectado" - generalmente cumple
            if (valorLower.contains("ausencia") || 
                valorLower.contains("nd") || 
                valorLower.equals("no detectado")) {
                return true;
            }

            // Caso: Valores con "<" (menor que el límite de detección)
            if (valorIngresado.startsWith("<")) {
                // Extraer el número después de "<"
                String numStr = valorIngresado.substring(1).trim();
                Optional<Double> valorOpt = parsearValorNumerico(numStr);
                Optional<Double> limiteOpt = parsearValorNumerico(limiteNormativa);

                if (valorOpt.isPresent() && limiteOpt.isPresent()) {
                    // Si es "< X", comparar X con el límite
                    return valorOpt.get() <= limiteOpt.get();
                }
                // Si no se puede parsear, asumir cumplimiento
                return true;
            }

            // Caso: Valores numéricos normales
            Optional<Double> valorOpt = parsearValorNumerico(valorIngresado);
            Optional<Double> limiteOpt = parsearValorNumerico(limiteNormativa);

            if (valorOpt.isPresent() && limiteOpt.isPresent()) {
                double valor = valorOpt.get();
                double limite = limiteOpt.get();

                // El parámetro cumple si es menor o igual al límite máximo
                boolean cumple = valor <= limite;

                logger.debug("Validación: {} {} (límite: {}) = {}",
                        valorIngresado, cumple ? "<=" : ">", limiteNormativa, cumple);

                return cumple;
            }

            // Si no se pudo parsear, registrar advertencia y asumir cumplimiento
            logger.warn("No se pudo parsear valor '{}' o límite '{}', asumiendo cumplimiento",
                    valorIngresado, limiteNormativa);
            return true;

        } catch (Exception e) {
            logger.error("Error al validar parámetro: valor={}, limite={}", 
                    valorIngresado, limiteNormativa, e);
            return true; // En caso de error, asumir cumplimiento
        }
    }

    /**
     * Parsea un valor numérico desde un String
     * Maneja formatos como "7.2", "150", "< 0.5", etc.
     */
    private Optional<Double> parsearValorNumerico(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return Optional.empty();
        }

        try {
            // Limpiar el string: quitar espacios, comas como separador de miles
            String cleaned = valor.trim()
                    .replace(",", "")  // Eliminar comas de miles
                    .replace("<", "")  // Eliminar símbolos
                    .replace(">", "")
                    .trim();

            // Intentar parsear como double
            return Optional.of(Double.parseDouble(cleaned));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Convierte MuestraAnalisis a TareaPendienteDTO
     */
    private TareaPendienteDTO convertirATareaPendienteDto(MuestraAnalisis ma) {
        TareaPendienteDTO dto = new TareaPendienteDTO();
        dto.setIdMuestraAnalisis(ma.getIdMuestraAnalisis());
        dto.setNumeroMuestra(ma.getMuestra().getNumeroInterno());
        dto.setCodigoBarras(ma.getMuestra().getCodigoBarras());
        dto.setNombreAnalisis(ma.getAnalisis().getNombreAnalisis());
        dto.setCodigoAnalisis(ma.getAnalisis().getCodigo());
        dto.setFechaAgregado(ma.getFechaAgregado());
        dto.setPrioridad(ma.getMuestra().getPrioridad().name());

        // Cliente anidado
        TareaPendienteDTO.ClienteBasicDTO clienteDTO = new TareaPendienteDTO.ClienteBasicDTO();
        clienteDTO.setIdCliente(ma.getMuestra().getCliente().getIdCliente());
        clienteDTO.setNombre(ma.getMuestra().getCliente().getNombreCliente());
        clienteDTO.setRazonSocial(ma.getMuestra().getCliente().getEmpresa());
        dto.setCliente(clienteDTO);

        return dto;
    }

    /**
     * Busca una tarea por código de barras QR en estado EN_PROCESO
     * Incluye los límites normativos del análisis
     */
    @Transactional(readOnly = true)
    public TareaPendienteDTO buscarPorCodigoBarras(String codigoBarras) {
        logger.info("Buscando tarea por código QR: {}", codigoBarras);

        List<MuestraAnalisis> tareas = muestraAnalisisRepository
                .findByMuestra_CodigoBarrasAndEstado(codigoBarras);

        if (tareas.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No se encontró ninguna tarea en proceso para el código: " + codigoBarras);
        }

        // Retornar la primera (debería ser única por muestra + estado)
        MuestraAnalisis tarea = tareas.get(0);
        TareaPendienteDTO dto = convertirATareaPendienteDto(tarea);

        // Agregar límites normativos del análisis
        UUID analisisId = tarea.getAnalisis().getIdAnalisis();
        List<Parametro> parametros = parametroRepository.findByAnalisisId(analisisId);

        // Obtener el parámetro principal (si existe)
        if (!parametros.isEmpty()) {
            Parametro parametroPrincipal = parametros.get(0);
            dto.setLimiteMinimoNormativa(parametroPrincipal.getValorMinimoNormativa());
            dto.setLimiteMaximoNormativa(parametroPrincipal.getValorMaximoNormativa());

        }

        dto.setCumpleNorma(tarea.getCumpleNorma());

        return dto;
    }

    /**
     * Agrupa tareas pendientes sin OT por nombre de análisis
     * Retorna un mapa con el conteo: {"pH": 15, "Cloro": 8}
     */
    @Transactional(readOnly = true)
    public Map<String, Long> contarTareasPendientesPorAnalisis() {
        logger.info("Obteniendo conteo de tareas pendientes agrupadas por análisis");

        List<Object[]> resultados = muestraAnalisisRepository.countTareasPendientesPorAnalisis();

        Map<String, Long> mapa = new HashMap<>();
        for (Object[] resultado : resultados) {
            String nombreAnalisis = (String) resultado[0];
            Long cantidad = (Long) resultado[1];
            mapa.put(nombreAnalisis, cantidad);
        }

        logger.info("Se encontraron {} tipos de análisis con tareas pendientes", mapa.size());
        return mapa;
    }

    /**
     * Guarda un resultado simple (un solo valor) capturado desde el modal de captura.
     * Internamente construye el ResultadoUpdateDTO y delega a actualizarResultado().
     */
    public Map<String, Object> guardarResultadoSimple(GuardarResultadoSimpleDTO dto) {
        UUID tareaId = dto.getIdMuestraAnalisis();
        logger.info("Guardando resultado simple para tarea: {}", tareaId);

        // Buscar la tarea
        MuestraAnalisis tarea = muestraAnalisisRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarea no encontrada con ID: " + tareaId));

        // Obtener los parámetros del análisis
        UUID analisisId = tarea.getAnalisis().getIdAnalisis();
        List<Parametro> parametrosDelAnalisis = parametroRepository.findByAnalisisId(analisisId);

        // Construir el ResultadoUpdateDTO a partir del valor simple
        ResultadoUpdateDTO resultadoDTO = new ResultadoUpdateDTO();
        Map<String, ResultadoUpdateDTO.ParametroResultadoDTO> parametrosMap = new HashMap<>();

        if (!parametrosDelAnalisis.isEmpty()) {
            Parametro parametroPrincipal = parametrosDelAnalisis.get(0);
            parametrosMap.put(
                parametroPrincipal.getNombre(),
                new ResultadoUpdateDTO.ParametroResultadoDTO(
                    dto.getValorMedido(),
                    parametroPrincipal.getUnidad()
                )
            );
        } else {
            // Caso sin parámetros configurados de forma explícita: usar nombre análisis
            String nombreAnalisis = tarea.getAnalisis().getNombreAnalisis();
            parametrosMap.put(
                nombreAnalisis,
                new ResultadoUpdateDTO.ParametroResultadoDTO(
                    dto.getValorMedido(),
                    null // Unidad desconocida
                )
            );
        }

        // Si hay más parámetros, los dejamos sin valor para que no se marque como completo prematuramente
        resultadoDTO.setParametros(parametrosMap);

        // Agregar observaciones como metadata si se proporcionaron
        if (dto.getObservaciones() != null && !dto.getObservaciones().isBlank()) {
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("observaciones", dto.getObservaciones());
            metadata.put("fecha_analisis", java.time.LocalDateTime.now().toString());
            metadata.put("version_metodo", "NCh409/1:2005");
            metadata.put("codigo_barras_capturado", dto.getCodigoBarras());
            resultadoDTO.setMetadata(metadata);
        }

        // Delegar a la lógica existente de validación NCh 409
        Map<String, Object> response = actualizarResultado(tareaId, resultadoDTO);

        // Enriquecer respuesta con info adicional
        response.put("tarea_id", tareaId.toString());
        response.put("numero_muestra", tarea.getMuestra().getNumeroInterno());
        response.put("nombre_analisis", tarea.getAnalisis().getNombreAnalisis());

        return response;
    }

    /**
     * Valida una tarea, cambiando su estado de COMPLETADO a VALIDADO
     */
    public void validarTarea(UUID tareaId) {
        logger.info("Validando tarea: {}", tareaId);

        MuestraAnalisis tarea = muestraAnalisisRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarea no encontrada con ID: " + tareaId));

        // Validar que esté en estado COMPLETADO
        if (tarea.getEstadoAnalisis() != MuestraAnalisis.EstadoAnalisis.COMPLETADO) {
            throw new ValidationException(
                    "Solo se pueden validar tareas en estado COMPLETADO. Estado actual: " 
                    + tarea.getEstadoAnalisis());
        }

        // Cambiar a VALIDADO
        tarea.validar();
        muestraAnalisisRepository.save(tarea);

        logger.info("Tarea {} validada exitosamente", tareaId);
    }

    /**
     * Rechaza una tarea
     */
    public void rechazarTarea(UUID tareaId) {
        logger.info("Rechazando tarea: {}", tareaId);

        MuestraAnalisis tarea = muestraAnalisisRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tarea no encontrada con ID: " + tareaId));

        tarea.setCumpleNormativa(false);
        tarea.setEstadoAnalisis(MuestraAnalisis.EstadoAnalisis.CANCELADO);
        muestraAnalisisRepository.save(tarea);
    }
}
