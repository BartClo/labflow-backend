package com.labflow.service;

import com.labflow.dto.ReactivoCreateDTO;
import com.labflow.dto.ReactivoDTO;
import com.labflow.model.Reactivo;
import com.labflow.repository.ReactivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para gestión de reactivos químicos
 */
@Service
@Transactional
public class ReactivoService {

    private final ReactivoRepository reactivoRepository;

    public ReactivoService(ReactivoRepository reactivoRepository) {
        this.reactivoRepository = reactivoRepository;
    }

    public List<ReactivoDTO> obtenerTodos() {
        return reactivoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public List<ReactivoDTO> obtenerActivos() {
        return reactivoRepository.findByActivoTrue().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public Optional<ReactivoDTO> obtenerPorId(UUID id) {
        return reactivoRepository.findById(id)
                .map(this::convertirADTO);
    }

    public Optional<ReactivoDTO> obtenerPorCodigo(String codigo) {
        return reactivoRepository.findByCodigo(codigo)
                .map(this::convertirADTO);
    }

    public List<ReactivoDTO> obtenerPorVencimientoCercano(LocalDate fecha) {
        return reactivoRepository.findByFechaVencimientoBeforeAndActivoTrue(fecha).stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ReactivoDTO crear(ReactivoCreateDTO createDTO) {
        Reactivo reactivo = new Reactivo();
        mapearDTOAEntidad(createDTO, reactivo);
        
        Reactivo reactivoGuardado = reactivoRepository.saveAndFlush(reactivo);
        return convertirADTO(reactivoGuardado);
    }

    public ReactivoDTO actualizar(UUID id, ReactivoCreateDTO createDTO) {
        Reactivo reactivo = reactivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reactivo no encontrado con ID: " + id));
        
        mapearDTOAEntidad(createDTO, reactivo);
        
        Reactivo reactivoActualizado = reactivoRepository.saveAndFlush(reactivo);
        return convertirADTO(reactivoActualizado);
    }

    public void eliminar(UUID id) {
        Reactivo reactivo = reactivoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reactivo no encontrado con ID: " + id));
        
        reactivo.setActivo(false);
        reactivoRepository.saveAndFlush(reactivo);
    }

    private ReactivoDTO convertirADTO(Reactivo reactivo) {
        ReactivoDTO dto = new ReactivoDTO();
        dto.setReactivoId(reactivo.getReactivoId());
        dto.setNombre(reactivo.getNombre());
        dto.setCodigo(reactivo.getCodigo());
        dto.setNumeroCas(reactivo.getNumeroCas());
        dto.setMarca(reactivo.getMarca());
        dto.setLote(reactivo.getLote());
        dto.setConcentracion(reactivo.getConcentracion());
        dto.setFechaVencimiento(reactivo.getFechaVencimiento());
        dto.setFechaApertura(reactivo.getFechaApertura());
        dto.setStockActual(reactivo.getStockActual());
        dto.setStockMinimo(reactivo.getStockMinimo());
        dto.setUnidadMedida(reactivo.getUnidadMedida());
        dto.setUbicacionAlmacenamiento(reactivo.getUbicacionAlmacenamiento());
        dto.setObservaciones(reactivo.getObservaciones());
        dto.setActivo(reactivo.getActivo());
        dto.setFechaCreacion(reactivo.getFechaCreacion());
        dto.setFechaActualizacion(reactivo.getFechaActualizacion());
        return dto;
    }

    private void mapearDTOAEntidad(ReactivoCreateDTO dto, Reactivo reactivo) {
        reactivo.setNombre(dto.getNombre());
        reactivo.setCodigo(dto.getCodigo());
        reactivo.setNumeroCas(dto.getNumeroCas());
        reactivo.setMarca(dto.getMarca());
        reactivo.setLote(dto.getLote());
        reactivo.setConcentracion(dto.getConcentracion());
        reactivo.setFechaVencimiento(dto.getFechaVencimiento());
        reactivo.setFechaApertura(dto.getFechaApertura());
        reactivo.setStockActual(dto.getStockActual());
        reactivo.setStockMinimo(dto.getStockMinimo());
        reactivo.setUnidadMedida(dto.getUnidadMedida());
        reactivo.setUbicacionAlmacenamiento(dto.getUbicacionAlmacenamiento());
        reactivo.setObservaciones(dto.getObservaciones());
        reactivo.setActivo(dto.getActivo() != null && dto.getActivo());
    }
}
