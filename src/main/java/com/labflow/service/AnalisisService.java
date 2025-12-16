package com.labflow.service;

import com.labflow.dto.AnalisisCreateDTO;
import com.labflow.dto.AnalisisDTO;
import com.labflow.model.Analisis;
import com.labflow.repository.AnalisisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para operaciones de negocio de Análisis
 */
@Service
@Transactional
public class AnalisisService {

    private final AnalisisRepository analisisRepository;

    public AnalisisService(AnalisisRepository analisisRepository) {
        this.analisisRepository = analisisRepository;
    }

    /**
     * Obtiene todos los análisis
     */
    @Transactional(readOnly = true)
    public List<AnalisisDTO> obtenerTodos() {
        return analisisRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Obtiene todos los análisis activos
     */
    @Transactional(readOnly = true)
    public List<AnalisisDTO> obtenerActivos() {
        return analisisRepository.findAllActivos()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Obtiene un análisis por ID
     */
    @Transactional(readOnly = true)
    public Optional<AnalisisDTO> obtenerPorId(UUID id) {
        return analisisRepository.findById(id)
                .map(this::convertirADto);
    }

    /**
     * Obtiene un análisis por código
     */
    @Transactional(readOnly = true)
    public Optional<AnalisisDTO> obtenerPorCodigo(String codigo) {
        return analisisRepository.findByCodigo(codigo)
                .map(this::convertirADto);
    }

    /**
     * Busca análisis por categoría
     */
    @Transactional(readOnly = true)
    public List<AnalisisDTO> buscarPorCategoria(String categoria) {
        return analisisRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Busca análisis por nombre
     */
    @Transactional(readOnly = true)
    public List<AnalisisDTO> buscarPorNombre(String nombre) {
        return analisisRepository.findByNombreAnalisisContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Crea un nuevo análisis
     */
    public AnalisisDTO crear(AnalisisCreateDTO createDTO) {
        // Validar que el código no exista
        if (analisisRepository.existsByCodigo(createDTO.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un análisis con el código: " + createDTO.getCodigo());
        }

        // Crear entidad - NO establecer el ID manualmente, dejar que Hibernate lo genere
        Analisis analisis = new Analisis();
        mapearDesdeDtoCreate(createDTO, analisis);
        // Las fechas se establecen automáticamente con @CreationTimestamp y @UpdateTimestamp

        // Guardar y forzar flush para que Hibernate aplique los timestamps
        Analisis analisisGuardado = analisisRepository.saveAndFlush(analisis);
        return convertirADto(analisisGuardado);
    }

    /**
     * Actualiza un análisis existente
     */
    public Optional<AnalisisDTO> actualizar(UUID id, AnalisisCreateDTO updateDTO) {
        return analisisRepository.findById(id)
                .map(analisis -> {
                    // Validar que el código no exista en otro análisis
                    if (analisisRepository.existsByCodigoAndIdAnalisisNot(updateDTO.getCodigo(), id)) {
                        throw new IllegalArgumentException("Ya existe otro análisis con el código: " + updateDTO.getCodigo());
                    }

                    // Actualizar campos
                    mapearDesdeDtoCreate(updateDTO, analisis);
                    // La fecha de actualización se establece automáticamente con @UpdateTimestamp

                    // Guardar y forzar flush para que Hibernate aplique los timestamps
                    Analisis analisisActualizado = analisisRepository.saveAndFlush(analisis);
                    return convertirADto(analisisActualizado);
                });
    }

    /**
     * Elimina un análisis
     */
    public boolean eliminar(UUID id) {
        if (analisisRepository.existsById(id)) {
            analisisRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Cambia el estado de un análisis
     */
    public Optional<AnalisisDTO> cambiarEstado(UUID id, String nuevoEstado) {
        return analisisRepository.findById(id)
                .map(analisis -> {
                    analisis.setEstado(nuevoEstado);
                    // La fecha de actualización se establece automáticamente con @UpdateTimestamp
                    Analisis analisisActualizado = analisisRepository.saveAndFlush(analisis);
                    return convertirADto(analisisActualizado);
                });
    }

    /**
     * Convierte entidad a DTO
     */
    private AnalisisDTO convertirADto(Analisis analisis) {
        AnalisisDTO dto = new AnalisisDTO();
        dto.setIdAnalisis(analisis.getIdAnalisis());
        dto.setNombreAnalisis(analisis.getNombreAnalisis());
        dto.setCodigo(analisis.getCodigo());
        dto.setDescripcion(analisis.getDescripcion());
        dto.setCategoria(analisis.getCategoria());
        dto.setMetodoEnsayo(analisis.getMetodoEnsayo());
        dto.setTiposMuestraAplicables(analisis.getTiposMuestraAplicables());
        dto.setParametrosMedir(analisis.getParametrosMedir());
        dto.setEquiposRequeridos(analisis.getEquiposRequeridos());
        dto.setInsumosRequeridos(analisis.getInsumosRequeridos());
        dto.setReactivosRequeridos(analisis.getReactivosRequeridos());
        dto.setDuracionEstimadaHoras(analisis.getDuracionEstimadaHoras());
        dto.setPrecioClp(analisis.getPrecioClp());
        dto.setDiasEntrega(analisis.getDiasEntrega());
        dto.setEstado(analisis.getEstado());
        dto.setFechaCreacion(analisis.getFechaCreacion());
        dto.setFechaActualizacion(analisis.getFechaActualizacion());
        return dto;
    }

    /**
     * Mapea campos desde CreateDTO a entidad
     */
    private void mapearDesdeDtoCreate(AnalisisCreateDTO dto, Analisis analisis) {
        analisis.setNombreAnalisis(dto.getNombreAnalisis());
        analisis.setCodigo(dto.getCodigo());
        analisis.setDescripcion(dto.getDescripcion());
        analisis.setCategoria(dto.getCategoria());
        analisis.setMetodoEnsayo(dto.getMetodoEnsayo());
        analisis.setTiposMuestraAplicables(dto.getTiposMuestraAplicables());
        analisis.setParametrosMedir(dto.getParametrosMedir());
        analisis.setEquiposRequeridos(dto.getEquiposRequeridos());
        analisis.setInsumosRequeridos(dto.getInsumosRequeridos());
        analisis.setReactivosRequeridos(dto.getReactivosRequeridos());
        analisis.setDuracionEstimadaHoras(dto.getDuracionEstimadaHoras());
        analisis.setPrecioClp(dto.getPrecioClp());
        analisis.setDiasEntrega(dto.getDiasEntrega());
        analisis.setEstado(dto.getEstado());
    }
}