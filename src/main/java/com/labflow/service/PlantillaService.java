package com.labflow.service;

import com.labflow.dto.PlantillaCreateDTO;
import com.labflow.dto.PlantillaDTO;
import com.labflow.model.Analisis;
import com.labflow.model.Plantilla;
import com.labflow.model.PlantillaAnalisis;
import com.labflow.repository.AnalisisRepository;
import com.labflow.repository.PlantillaAnalisisRepository;
import com.labflow.repository.PlantillaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para operaciones de negocio de Plantillas
 */
@Service
@Transactional
public class PlantillaService {

    private final PlantillaRepository plantillaRepository;
    private final PlantillaAnalisisRepository plantillaAnalisisRepository;
    private final AnalisisRepository analisisRepository;

    public PlantillaService(PlantillaRepository plantillaRepository,
                           PlantillaAnalisisRepository plantillaAnalisisRepository,
                           AnalisisRepository analisisRepository) {
        this.plantillaRepository = plantillaRepository;
        this.plantillaAnalisisRepository = plantillaAnalisisRepository;
        this.analisisRepository = analisisRepository;
    }

    /**
     * Obtiene todas las plantillas
     */
    @Transactional(readOnly = true)
    public List<PlantillaDTO> obtenerTodas() {
        return plantillaRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Obtiene todas las plantillas activas
     */
    @Transactional(readOnly = true)
    public List<PlantillaDTO> obtenerActivas() {
        return plantillaRepository.findAllActivas()
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Obtiene una plantilla por ID
     */
    @Transactional(readOnly = true)
    public Optional<PlantillaDTO> obtenerPorId(UUID id) {
        return plantillaRepository.findById(id)
                .map(this::convertirADto);
    }

    /**
     * Busca plantillas por nombre
     */
    @Transactional(readOnly = true)
    public List<PlantillaDTO> buscarPorNombre(String nombre) {
        return plantillaRepository.findByNombrePlantillaContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Busca plantillas por tipo de muestra
     */
    @Transactional(readOnly = true)
    public List<PlantillaDTO> buscarPorTipoMuestra(String tipoMuestra) {
        return plantillaRepository.findByTipoMuestra(tipoMuestra)
                .stream()
                .map(this::convertirADto)
                .toList();
    }

    /**
     * Crea una nueva plantilla
     */
    public PlantillaDTO crear(PlantillaCreateDTO createDTO) {
        // Validar que el nombre no exista
        if (plantillaRepository.existsByNombrePlantilla(createDTO.getNombrePlantilla())) {
            throw new IllegalArgumentException("Ya existe una plantilla con el nombre: " + createDTO.getNombrePlantilla());
        }

        // Validar campos de paquete comercial
        if (Boolean.TRUE.equals(createDTO.getEsPaqueteComercial()) && 
            (createDTO.getPrecioPaquete() == null || createDTO.getPrecioPaquete().compareTo(java.math.BigDecimal.ZERO) <= 0)) {
            throw new IllegalArgumentException("El precio del paquete es obligatorio y debe ser mayor a 0 para paquetes comerciales");
        }

        // Crear entidad plantilla
        Plantilla plantilla = new Plantilla();
        // El ID se genera automáticamente con @GeneratedValue
        mapearDesdeDtoCreate(createDTO, plantilla);
        // Las fechas se setean automáticamente con @CreationTimestamp y @UpdateTimestamp

        // Guardar plantilla
        Plantilla plantillaGuardada = plantillaRepository.save(plantilla);

        // Agregar análisis a la plantilla si se especificaron
        if (createDTO.getAnalisisIncluidos() != null && !createDTO.getAnalisisIncluidos().isEmpty()) {
            agregarAnalisisAPlantilla(plantillaGuardada.getIdPlantilla(), createDTO.getAnalisisIncluidos());
        }

        return convertirADto(plantillaGuardada);
    }

    /**
     * Actualiza una plantilla existente
     */
    public Optional<PlantillaDTO> actualizar(UUID id, PlantillaCreateDTO updateDTO) {
        return plantillaRepository.findById(id)
                .map(plantilla -> {
                    // Validar que el nombre no exista en otra plantilla
                    if (plantillaRepository.existsByNombrePlantillaAndIdPlantillaNot(updateDTO.getNombrePlantilla(), id)) {
                        throw new IllegalArgumentException("Ya existe otra plantilla con el nombre: " + updateDTO.getNombrePlantilla());
                    }

                    // Validar campos de paquete comercial
                    if (Boolean.TRUE.equals(updateDTO.getEsPaqueteComercial()) && 
                        (updateDTO.getPrecioPaquete() == null || updateDTO.getPrecioPaquete().compareTo(java.math.BigDecimal.ZERO) <= 0)) {
                        throw new IllegalArgumentException("El precio del paquete es obligatorio y debe ser mayor a 0 para paquetes comerciales");
                    }

                    // Actualizar campos básicos
                    mapearDesdeDtoCreate(updateDTO, plantilla);
                    // La fechaActualizacion se setea automáticamente con @UpdateTimestamp

                    // Actualizar análisis incluidos
                    if (updateDTO.getAnalisisIncluidos() != null) {
                        // Eliminar análisis actuales
                        plantillaAnalisisRepository.deleteByPlantillaIdPlantilla(id);
                        // Agregar nuevos análisis
                        if (!updateDTO.getAnalisisIncluidos().isEmpty()) {
                            agregarAnalisisAPlantilla(id, updateDTO.getAnalisisIncluidos());
                        }
                    }

                    // Guardar
                    Plantilla plantillaActualizada = plantillaRepository.save(plantilla);
                    return convertirADto(plantillaActualizada);
                });
    }

    /**
     * Elimina una plantilla
     */
    public boolean eliminar(UUID id) {
        if (plantillaRepository.existsById(id)) {
            plantillaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Cambia el estado de una plantilla
     */
    public Optional<PlantillaDTO> cambiarEstado(UUID id, String nuevoEstado) {
        return plantillaRepository.findById(id)
                .map(plantilla -> {
                    plantilla.setEstado(nuevoEstado);
                    // La fechaActualizacion se setea automáticamente con @UpdateTimestamp
                    Plantilla plantillaActualizada = plantillaRepository.save(plantilla);
                    return convertirADto(plantillaActualizada);
                });
    }

    /**
     * Agrega un análisis a una plantilla
     */
    public Optional<PlantillaDTO> agregarAnalisis(UUID idPlantilla, UUID idAnalisis, Integer orden) {
        return plantillaRepository.findById(idPlantilla)
                .map(plantilla -> {
                    // Verificar que el análisis existe
                    Analisis analisis = analisisRepository.findById(idAnalisis)
                            .orElseThrow(() -> new IllegalArgumentException("Análisis no encontrado"));

                    // Verificar que no exista ya la relación
                    if (plantillaAnalisisRepository.existsByPlantillaIdPlantillaAndAnalisisIdAnalisis(idPlantilla, idAnalisis)) {
                        throw new IllegalArgumentException("El análisis ya está incluido en esta plantilla");
                    }

                    // Si no se especifica orden, usar el siguiente disponible
                    Integer ordenFinal = orden;
                    if (ordenFinal == null) {
                        ordenFinal = plantillaAnalisisRepository.findMaxOrdenByPlantillaId(idPlantilla) + 1;
                    }

                    // Crear relación
                    PlantillaAnalisis plantillaAnalisis = new PlantillaAnalisis(plantilla, analisis, ordenFinal);
                    plantillaAnalisisRepository.save(plantillaAnalisis);

                    return convertirADto(plantilla);
                });
    }

    /**
     * Remueve un análisis de una plantilla
     */
    public Optional<PlantillaDTO> removerAnalisis(UUID idPlantilla, UUID idAnalisis) {
        return plantillaRepository.findById(idPlantilla)
                .map(plantilla -> {
                    plantillaAnalisisRepository.deleteByPlantillaIdPlantillaAndAnalisisIdAnalisis(idPlantilla, idAnalisis);
                    return convertirADto(plantilla);
                });
    }

    /**
     * Método auxiliar para agregar múltiples análisis a una plantilla
     */
    private void agregarAnalisisAPlantilla(UUID idPlantilla, List<PlantillaCreateDTO.AnalisisEnPlantillaDTO> analisisIncluidos) {
        Plantilla plantilla = plantillaRepository.findById(idPlantilla)
                .orElseThrow(() -> new IllegalArgumentException("Plantilla no encontrada"));

        for (PlantillaCreateDTO.AnalisisEnPlantillaDTO analisisDto : analisisIncluidos) {
            Analisis analisis = analisisRepository.findById(analisisDto.getAnalisisId())
                    .orElseThrow(() -> new IllegalArgumentException("Análisis no encontrado: " + analisisDto.getAnalisisId()));

            PlantillaAnalisis plantillaAnalisis = new PlantillaAnalisis(plantilla, analisis, analisisDto.getOrden());
            plantillaAnalisisRepository.save(plantillaAnalisis);
        }
    }

    /**
     * Convierte entidad a DTO
     */
    private PlantillaDTO convertirADto(Plantilla plantilla) {
        PlantillaDTO dto = new PlantillaDTO();
        dto.setIdPlantilla(plantilla.getIdPlantilla());
        dto.setNombrePlantilla(plantilla.getNombrePlantilla());
        dto.setDescripcion(plantilla.getDescripcion());
        dto.setEstado(plantilla.getEstado());
        dto.setTiposMuestraAplicables(plantilla.getTiposMuestraAplicables());
        dto.setEsPaqueteComercial(plantilla.getEsPaqueteComercial());
        dto.setPrecioPaquete(plantilla.getPrecioPaquete());
        dto.setCodigoPaquete(plantilla.getCodigoPaquete());
        dto.setFechaCreacion(plantilla.getFechaCreacion());
        dto.setFechaActualizacion(plantilla.getFechaActualizacion());

        // Obtener análisis incluidos
        List<PlantillaAnalisis> analisis = plantillaAnalisisRepository
                .findByPlantillaIdPlantillaOrderByOrdenEnPlantillaAsc(plantilla.getIdPlantilla());
        
        List<PlantillaDTO.AnalisisEnPlantillaResponseDTO> analisisResponse = analisis.stream()
                .map(pa -> {
                    PlantillaDTO.AnalisisEnPlantillaResponseDTO responseDto = new PlantillaDTO.AnalisisEnPlantillaResponseDTO();
                    responseDto.setAnalisisId(pa.getAnalisis().getIdAnalisis());
                    responseDto.setCodigo(pa.getAnalisis().getCodigo());
                    responseDto.setNombreAnalisis(pa.getAnalisis().getNombreAnalisis());
                    responseDto.setCategoria(pa.getAnalisis().getCategoria());
                    responseDto.setOrdenEnPlantilla(pa.getOrdenEnPlantilla());
                    responseDto.setFechaAgregado(pa.getFechaAgregado());
                    return responseDto;
                })
                .toList();

        dto.setAnalisisIncluidos(analisisResponse);
        return dto;
    }

    /**
     * Mapea campos desde CreateDTO a entidad
     */
    private void mapearDesdeDtoCreate(PlantillaCreateDTO dto, Plantilla plantilla) {
        plantilla.setNombrePlantilla(dto.getNombrePlantilla());
        plantilla.setDescripcion(dto.getDescripcion());
        plantilla.setEstado(dto.getEstado());
        plantilla.setTiposMuestraAplicables(dto.getTiposMuestraAplicables());
        plantilla.setEsPaqueteComercial(dto.getEsPaqueteComercial());
        plantilla.setPrecioPaquete(dto.getPrecioPaquete());
        plantilla.setCodigoPaquete(dto.getCodigoPaquete());
    }
}