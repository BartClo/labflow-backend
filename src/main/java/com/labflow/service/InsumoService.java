package com.labflow.service;

import com.labflow.dto.InsumoCreateDTO;
import com.labflow.dto.InsumoDTO;
import com.labflow.model.Insumo;
import com.labflow.repository.InsumoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para gestión de insumos de laboratorio
 */
@Service
@Transactional
public class InsumoService {

    private final InsumoRepository insumoRepository;

    public InsumoService(InsumoRepository insumoRepository) {
        this.insumoRepository = insumoRepository;
    }

    public List<InsumoDTO> obtenerTodos() {
        return insumoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<InsumoDTO> obtenerActivos() {
        return insumoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Optional<InsumoDTO> obtenerPorId(UUID id) {
        return insumoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public InsumoDTO crear(InsumoCreateDTO createDTO) {
        Insumo insumo = new Insumo();
        mapearDTOAEntidad(createDTO, insumo);
        
        Insumo insumoGuardado = insumoRepository.saveAndFlush(insumo);
        return convertirADTO(insumoGuardado);
    }

    public InsumoDTO actualizar(UUID id, InsumoCreateDTO createDTO) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));
        
        mapearDTOAEntidad(createDTO, insumo);
        
        Insumo insumoActualizado = insumoRepository.saveAndFlush(insumo);
        return convertirADTO(insumoActualizado);
    }

    public void eliminar(UUID id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));
        
        insumo.setActivo(false);
        insumoRepository.saveAndFlush(insumo);
    }

    private InsumoDTO convertirADTO(Insumo insumo) {
        InsumoDTO dto = new InsumoDTO();
        dto.setInsumoId(insumo.getInsumoId());
        dto.setNombre(insumo.getNombre());
        dto.setMarca(insumo.getMarca());
        dto.setDescripcion(insumo.getDescripcion());
        dto.setStockActual(insumo.getStockActual());
        dto.setStockMinimo(insumo.getStockMinimo());
        dto.setUnidadMedida(insumo.getUnidadMedida());
        dto.setUbicacionAlmacenamiento(insumo.getUbicacionAlmacenamiento());
        dto.setObservaciones(insumo.getObservaciones());
        dto.setActivo(insumo.getActivo());
        dto.setFechaCreacion(insumo.getFechaCreacion());
        dto.setFechaActualizacion(insumo.getFechaActualizacion());
        return dto;
    }

    private void mapearDTOAEntidad(InsumoCreateDTO dto, Insumo insumo) {
        insumo.setNombre(dto.getNombre());
        insumo.setMarca(dto.getMarca());
        insumo.setDescripcion(dto.getDescripcion());
        insumo.setStockActual(dto.getStockActual());
        insumo.setStockMinimo(dto.getStockMinimo());
        insumo.setUnidadMedida(dto.getUnidadMedida());
        insumo.setUbicacionAlmacenamiento(dto.getUbicacionAlmacenamiento());
        insumo.setObservaciones(dto.getObservaciones());
        insumo.setActivo(dto.getActivo() != null && dto.getActivo());
    }
}
