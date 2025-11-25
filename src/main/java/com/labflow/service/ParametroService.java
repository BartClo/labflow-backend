package com.labflow.service;

import com.labflow.dto.*;
import com.labflow.model.Analisis;
import com.labflow.model.ConfigControlCalidad;
import com.labflow.model.Parametro;
import com.labflow.repository.AnalisisRepository;
import com.labflow.repository.ParametroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar parámetros de análisis
 */
@Service
@Transactional
public class ParametroService {

    private final ParametroRepository parametroRepository;
    private final AnalisisRepository analisisRepository;

    @Autowired
    public ParametroService(ParametroRepository parametroRepository, AnalisisRepository analisisRepository) {
        this.parametroRepository = parametroRepository;
        this.analisisRepository = analisisRepository;
    }

    /**
     * Obtiene todos los parámetros
     */
    public List<ParametroDTO> obtenerTodos() {
        return parametroRepository.findAll().stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un parámetro por su ID
     */
    public Optional<ParametroDTO> obtenerPorId(UUID id) {
        return parametroRepository.findById(id)
                .map(this::convertirADto);
    }

    /**
     * Obtiene todos los parámetros de un análisis específico
     */
    public List<ParametroDTO> obtenerPorAnalisisId(UUID idAnalisis) {
        return parametroRepository.findByAnalisisId(idAnalisis).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Busca parámetros por nombre (búsqueda parcial)
     */
    public List<ParametroDTO> buscarPorNombre(String nombre) {
        return parametroRepository.findByNombreContaining(nombre).stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un parámetro específico de un análisis por nombre
     */
    public Optional<ParametroDTO> obtenerPorAnalisisIdYNombre(UUID idAnalisis, String nombre) {
        return parametroRepository.findByAnalisisIdAndNombre(idAnalisis, nombre)
                .map(this::convertirADto);
    }

    /**
     * Cuenta los parámetros de un análisis
     */
    public long contarPorAnalisisId(UUID idAnalisis) {
        return parametroRepository.countByAnalisisId(idAnalisis);
    }

    /**
     * Crea un nuevo parámetro
     */
    public ParametroDTO crear(ParametroCreateDTO createDTO) {
        // Verificar que el análisis existe
        Analisis analisis = analisisRepository.findById(createDTO.getIdAnalisis())
                .orElseThrow(() -> new IllegalArgumentException("No existe el análisis con ID: " + createDTO.getIdAnalisis()));

        // Verificar que no exista ya un parámetro con el mismo nombre para este análisis
        if (parametroRepository.findByAnalisisIdAndNombre(createDTO.getIdAnalisis(), createDTO.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un parámetro con el nombre '" + createDTO.getNombre() + 
                                             "' para el análisis '" + analisis.getNombreAnalisis() + "'");
        }

        // Crear el parámetro
        Parametro parametro = new Parametro();
        parametro.setId(UUID.randomUUID());
        parametro.setAnalisis(analisis);
        parametro.setNombre(createDTO.getNombre());
        parametro.setUnidad(createDTO.getUnidad());
        parametro.setValorMaximoNormativa(createDTO.getValorMaximoNormativa());

        // Agregar configuraciones de control de calidad si existen
        if (createDTO.getConfiguracionesControl() != null && !createDTO.getConfiguracionesControl().isEmpty()) {
            for (ConfigControlCalidadCreateDTO configDTO : createDTO.getConfiguracionesControl()) {
                ConfigControlCalidad config = new ConfigControlCalidad(
                    UUID.randomUUID(),
                    parametro,
                    configDTO.getTipoControl(),
                    configDTO.getRecuperacionMin(),
                    configDTO.getRecuperacionMax()
                );
                parametro.addConfiguracionControl(config);
            }
        }

        // Guardar
        Parametro parametroGuardado = parametroRepository.save(parametro);
        return convertirADto(parametroGuardado);
    }

    /**
     * Actualiza un parámetro existente
     */
    public Optional<ParametroDTO> actualizar(UUID id, ParametroCreateDTO updateDTO) {
        return parametroRepository.findById(id)
                .map(parametro -> {
                    // Verificar que el análisis existe
                    Analisis analisis = analisisRepository.findById(updateDTO.getIdAnalisis())
                            .orElseThrow(() -> new IllegalArgumentException("No existe el análisis con ID: " + updateDTO.getIdAnalisis()));

                    // Verificar que no exista otro parámetro con el mismo nombre para este análisis
                    Optional<Parametro> existente = parametroRepository.findByAnalisisIdAndNombre(
                            updateDTO.getIdAnalisis(), 
                            updateDTO.getNombre()
                    );
                    if (existente.isPresent() && !existente.get().getId().equals(id)) {
                        throw new IllegalArgumentException("Ya existe otro parámetro con el nombre '" + updateDTO.getNombre() + 
                                                         "' para el análisis '" + analisis.getNombreAnalisis() + "'");
                    }

                    // Actualizar campos básicos
                    parametro.setAnalisis(analisis);
                    parametro.setNombre(updateDTO.getNombre());
                    parametro.setUnidad(updateDTO.getUnidad());
                    parametro.setValorMaximoNormativa(updateDTO.getValorMaximoNormativa());

                    // Actualizar configuraciones de control de calidad
                    // Eliminar las existentes
                    parametro.getConfiguracionesControl().clear();

                    // Agregar las nuevas
                    if (updateDTO.getConfiguracionesControl() != null && !updateDTO.getConfiguracionesControl().isEmpty()) {
                        for (ConfigControlCalidadCreateDTO configDTO : updateDTO.getConfiguracionesControl()) {
                            ConfigControlCalidad config = new ConfigControlCalidad(
                                UUID.randomUUID(),
                                parametro,
                                configDTO.getTipoControl(),
                                configDTO.getRecuperacionMin(),
                                configDTO.getRecuperacionMax()
                            );
                            parametro.addConfiguracionControl(config);
                        }
                    }

                    // Guardar
                    Parametro parametroActualizado = parametroRepository.save(parametro);
                    return convertirADto(parametroActualizado);
                });
    }

    /**
     * Elimina un parámetro
     */
    public boolean eliminar(UUID id) {
        if (parametroRepository.existsById(id)) {
            parametroRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Convierte entidad Parametro a DTO
     */
    private ParametroDTO convertirADto(Parametro parametro) {
        ParametroDTO dto = new ParametroDTO();
        dto.setId(parametro.getId());
        dto.setIdAnalisis(parametro.getAnalisis().getIdAnalisis());
        dto.setNombre(parametro.getNombre());
        dto.setUnidad(parametro.getUnidad());
        dto.setValorMaximoNormativa(parametro.getValorMaximoNormativa());

        // Convertir configuraciones de control de calidad
        if (parametro.getConfiguracionesControl() != null) {
            List<ConfigControlCalidadDTO> configuracionesDTO = parametro.getConfiguracionesControl().stream()
                    .map(this::convertirConfigControlADto)
                    .collect(Collectors.toList());
            dto.setConfiguracionesControl(configuracionesDTO);
        }

        return dto;
    }

    /**
     * Convierte entidad ConfigControlCalidad a DTO
     */
    private ConfigControlCalidadDTO convertirConfigControlADto(ConfigControlCalidad config) {
        return new ConfigControlCalidadDTO(
                config.getId(),
                config.getTipoControl(),
                config.getRecuperacionMin(),
                config.getRecuperacionMax()
        );
    }
}
