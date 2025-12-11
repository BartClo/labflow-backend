package com.labflow.service;

import com.labflow.dto.EquipoCreateDTO;
import com.labflow.dto.EquipoDTO;
import com.labflow.model.Equipo;
import com.labflow.repository.EquipoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio para gestión de equipos de laboratorio
 */
@Service
@Transactional
public class EquipoService {

    private final EquipoRepository equipoRepository;

    public EquipoService(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    /**
     * Obtener todos los equipos
     */
    public List<EquipoDTO> obtenerTodos() {
        return equipoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener equipos activos
     */
    public List<EquipoDTO> obtenerActivos() {
        return equipoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener equipo por ID
     */
    public Optional<EquipoDTO> obtenerPorId(UUID id) {
        return equipoRepository.findById(id)
                .map(this::convertirADTO);
    }

    /**
     * Obtener equipo por código
     */
    public Optional<EquipoDTO> obtenerPorCodigo(String codigo) {
        return equipoRepository.findByCodigo(codigo)
                .map(this::convertirADTO);
    }

    /**
     * Buscar equipos por marca
     */
    public List<EquipoDTO> buscarPorMarca(String marca) {
        return equipoRepository.findByMarcaContainingIgnoreCaseAndActivoTrue(marca).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Crear nuevo equipo
     */
    public EquipoDTO crear(EquipoCreateDTO createDTO) {
        Equipo equipo = new Equipo();
        mapearDTOAEntidad(createDTO, equipo);
        
        Equipo equipoGuardado = equipoRepository.saveAndFlush(equipo);
        return convertirADTO(equipoGuardado);
    }

    /**
     * Actualizar equipo existente
     */
    public EquipoDTO actualizar(UUID id, EquipoCreateDTO createDTO) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
        
        mapearDTOAEntidad(createDTO, equipo);
        
        Equipo equipoActualizado = equipoRepository.saveAndFlush(equipo);
        return convertirADTO(equipoActualizado);
    }

    /**
     * Eliminar equipo (soft delete)
     */
    public void eliminar(UUID id) {
        Equipo equipo = equipoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado con ID: " + id));
        
        equipo.setActivo(false);
        equipoRepository.saveAndFlush(equipo);
    }

    /**
     * Convertir entidad a DTO
     */
    private EquipoDTO convertirADTO(Equipo equipo) {
        EquipoDTO dto = new EquipoDTO();
        dto.setEquipoId(equipo.getEquipoId());
        dto.setNombre(equipo.getNombre());
        dto.setCodigo(equipo.getCodigo());
        dto.setMarca(equipo.getMarca());
        dto.setModelo(equipo.getModelo());
        dto.setNumeroSerie(equipo.getNumeroSerie());
        dto.setUbicacion(equipo.getUbicacion());
        dto.setFechaCalibracion(equipo.getFechaCalibracion());
        dto.setProximaCalibracion(equipo.getProximaCalibracion());
        dto.setObservaciones(equipo.getObservaciones());
        dto.setActivo(equipo.getActivo());
        dto.setFechaCreacion(equipo.getFechaCreacion());
        dto.setFechaActualizacion(equipo.getFechaActualizacion());
        return dto;
    }

    /**
     * Mapear DTO a entidad
     */
    private void mapearDTOAEntidad(EquipoCreateDTO dto, Equipo equipo) {
        equipo.setNombre(dto.getNombre());
        equipo.setCodigo(dto.getCodigo());
        equipo.setMarca(dto.getMarca());
        equipo.setModelo(dto.getModelo());
        equipo.setNumeroSerie(dto.getNumeroSerie());
        equipo.setUbicacion(dto.getUbicacion());
        equipo.setFechaCalibracion(dto.getFechaCalibracion());
        equipo.setProximaCalibracion(dto.getProximaCalibracion());
        equipo.setObservaciones(dto.getObservaciones());
        equipo.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
    }
}
