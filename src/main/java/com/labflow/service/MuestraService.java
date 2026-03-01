package com.labflow.service;

import com.labflow.dto.MuestraCreateDTO;
import com.labflow.dto.MuestraDTO;
import com.labflow.exception.ResourceNotFoundException;
import com.labflow.exception.ValidationException;
import com.labflow.model.*;
import com.labflow.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para la gestión de muestras
 * Contiene toda la lógica de negocio para CRUD de muestras y análisis
 */
@Service
@Transactional
public class MuestraService {

    private static final String MUESTRA_NO_ENCONTRADA = "Muestra no encontrada con ID: ";

    private final MuestraRepository muestraRepository;
    private final MuestraAnalisisRepository muestraAnalisisRepository;
    private final MuestraPlantillaRepository muestraPlantillaRepository;
    private final ClientRepository clientRepository;
    private final AnalisisRepository analisisRepository;
    private final PlantillaRepository plantillaRepository;

    public MuestraService(MuestraRepository muestraRepository,
                         MuestraAnalisisRepository muestraAnalisisRepository,
                         MuestraPlantillaRepository muestraPlantillaRepository,
                         ClientRepository clientRepository,
                         AnalisisRepository analisisRepository,
                         PlantillaRepository plantillaRepository) {
        this.muestraRepository = muestraRepository;
        this.muestraAnalisisRepository = muestraAnalisisRepository;
        this.muestraPlantillaRepository = muestraPlantillaRepository;
        this.clientRepository = clientRepository;
        this.analisisRepository = analisisRepository;
        this.plantillaRepository = plantillaRepository;
    }

    /**
     * Generar código único para la muestra con formato M-AAAA-Correlativo
     * Ejemplo: M-2025-0001, M-2025-0002, etc.
     */
    private String generarCodigoUnico() {
        // Obtener año actual
        int anioActual = LocalDateTime.now().getYear();
        String prefijo = "M-" + anioActual + "-";
        
        // Buscar última muestra del año
        Optional<Muestra> ultimaMuestra = muestraRepository
                .findTopByNumeroInternoStartingWithOrderByNumeroInternoDesc(prefijo);
        
        int nuevoCorrelativo = 1;
        
        if (ultimaMuestra.isPresent()) {
            // Extraer el correlativo del último código (ej: M-2025-0045 -> 45)
            String ultimoCodigo = ultimaMuestra.get().getNumeroInterno();
            String[] partes = ultimoCodigo.split("-");
            if (partes.length == 3) {
                try {
                    int ultimoCorrelativo = Integer.parseInt(partes[2]);
                    nuevoCorrelativo = ultimoCorrelativo + 1;
                } catch (NumberFormatException e) {
                    // Si hay error al parsear, iniciar en 1
                    nuevoCorrelativo = 1;
                }
            }
        }
        
        // Formatear con 4 dígitos (0001, 0002, etc.)
        return String.format("%s%04d", prefijo, nuevoCorrelativo);
    }

    /**
     * Crear una nueva muestra con sus análisis y/o plantillas asociados
     */
    public MuestraDTO crearMuestra(MuestraCreateDTO createDTO) {
        // Generar código único si no se proporciona
        if (createDTO.getNumeroInterno() == null || createDTO.getNumeroInterno().trim().isEmpty()) {
            createDTO.setNumeroInterno(generarCodigoUnico());
        }
        
        // Validaciones
        validarDatosCreacion(createDTO);

        // Verificar que el cliente existe
        Client cliente = clientRepository.findById(createDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + createDTO.getIdCliente()));

        // Crear la muestra
        Muestra muestra = new Muestra();
        mapearDatosCreacion(createDTO, muestra, cliente);

        // Guardar la muestra
        Muestra muestraGuardada = muestraRepository.save(muestra);

        // Procesar análisis individuales si existen
        if (createDTO.getAnalisisIds() != null && !createDTO.getAnalisisIds().isEmpty()) {
            procesarAnalisisIndividuales(muestraGuardada, createDTO.getAnalisisIds());
        }

        // Procesar plantillas si existen
        if (createDTO.getPlantillaIds() != null && !createDTO.getPlantillaIds().isEmpty()) {
            procesarPlantillas(muestraGuardada, createDTO.getPlantillaIds());
        }

        // Recargar la muestra con todas las relaciones
        Muestra muestraFinal = muestraRepository.findById(muestraGuardada.getIdMuestra())
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraGuardada.getIdMuestra()));

        return convertirADTO(muestraFinal);
    }

    /**
     * Obtener muestra por ID
     */
    @Transactional(readOnly = true)
    public MuestraDTO obtenerMuestraPorId(UUID id) {
        Muestra muestra = muestraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + id));
        return convertirADTO(muestra);
    }

    /**
     * Obtener muestra por número interno
     */
    @Transactional(readOnly = true)
    public MuestraDTO obtenerMuestraPorNumeroInterno(String numeroInterno) {
        Muestra muestra = muestraRepository.findByNumeroInterno(numeroInterno)
                .orElseThrow(() -> new ResourceNotFoundException("Muestra no encontrada con número interno: " + numeroInterno));
        return convertirADTO(muestra);
    }

    /**
     * Listar todas las muestras con paginación
     */
    @Transactional(readOnly = true)
    public Page<MuestraDTO> listarMuestras(Pageable pageable) {
        Page<Muestra> muestras = muestraRepository.findAll(pageable);
        return muestras.map(this::convertirADTO);
    }

    /**
     * Buscar muestras por múltiples criterios
     */
    @Transactional(readOnly = true)
    public Page<MuestraDTO> buscarMuestras(UUID clienteId, String estado, String prioridad, 
                                          LocalDateTime fechaInicio, LocalDateTime fechaFin, 
                                          Pageable pageable) {
        // Parsear enums desde strings
        Muestra.EstadoMuestra estadoEnum = parseEstado(estado);
        Muestra.Prioridad prioridadEnum = parsePrioridad(prioridad);

        // Usar Specification para búsqueda dinámica
        Page<Muestra> muestras = muestraRepository.findAll(
                MuestraSpecification.buscarPorCriterios(clienteId, estadoEnum, prioridadEnum, fechaInicio, fechaFin),
                pageable);
        
        return muestras.map(this::convertirADTO);
    }

    /**
     * Búsqueda de texto libre
     */
    @Transactional(readOnly = true)
    public Page<MuestraDTO> buscarPorTexto(String texto, Pageable pageable) {
        Page<Muestra> muestras = muestraRepository.buscarPorTexto(texto, pageable);
        return muestras.map(this::convertirADTO);
    }

    /**
     * Actualizar muestra
     */
    public MuestraDTO actualizarMuestra(UUID id, MuestraCreateDTO updateDTO) {
        Muestra muestra = muestraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + id));

        // Validar datos de actualización
        validarDatosActualizacion(updateDTO, id);

        // Verificar cliente
        Client cliente = clientRepository.findById(updateDTO.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + updateDTO.getIdCliente()));

        // Actualizar datos básicos
        mapearDatosCreacion(updateDTO, muestra, cliente);

        // Actualizar análisis si han cambiado
        if (updateDTO.getAnalisisIds() != null && !updateDTO.getAnalisisIds().isEmpty()) {
            actualizarAnalisisMuestra(muestra, updateDTO.getAnalisisIds());
        }

        muestra = muestraRepository.save(muestra);
        return convertirADTO(muestra);
    }

    /**
     * Cambiar estado de la muestra
     */
    public MuestraDTO cambiarEstado(UUID id, String nuevoEstado) {
        Muestra muestra = muestraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + id));

        Muestra.EstadoMuestra estadoEnum = parseEstado(nuevoEstado);
        muestra.setEstado(estadoEnum);

        // Lógica adicional según el estado
        if (estadoEnum == Muestra.EstadoMuestra.EN_PROCESO) {
            if (muestra.getFechaRecepcion() == null) {
                muestra.setFechaRecepcion(LocalDateTime.now());
            }
        } else if (estadoEnum == Muestra.EstadoMuestra.COMPLETADA) {
            // Verificar que todos los análisis estén completados
            boolean todosCompletados = muestra.getMuestraAnalisis().stream()
                    .allMatch(ma -> ma.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.COMPLETADO);
            if (!todosCompletados) {
                throw new ValidationException("No se puede completar la muestra. Hay análisis pendientes.");
            }
        }

        muestra = muestraRepository.save(muestra);
        return convertirADTO(muestra);
    }

    public MuestraDTO cambiarPrioridad(UUID id, String nuevaPrioridad) {
        Muestra muestra = muestraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + id));

        Muestra.Prioridad prioridadEnum = parsePrioridad(nuevaPrioridad);
        if (prioridadEnum == null) {
            throw new ValidationException("La prioridad es requerida");
        }

        muestra.setPrioridad(prioridadEnum);
        muestra = muestraRepository.save(muestra);
        return convertirADTO(muestra);
    }

    /**
     * Agregar análisis a una muestra existente
     */
    public MuestraDTO agregarAnalisis(UUID muestraId, List<UUID> analisisIds) {
        Muestra muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId));

        List<MuestraAnalisis> nuevosAnalisis = new ArrayList<>();
        int siguienteOrden = muestraAnalisisRepository.getSiguienteOrdenEjecucion(muestraId);

        for (UUID analisisId : analisisIds) {
            // Verificar que no exista ya esta combinación
            if (muestraAnalisisRepository.existsByMuestraIdMuestraAndAnalisisIdAnalisis(muestraId, analisisId)) {
                continue; // Skip si ya existe
            }

            Analisis analisis = analisisRepository.findById(analisisId)
                    .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + analisisId));

            MuestraAnalisis muestraAnalisis = new MuestraAnalisis(muestra, analisis, siguienteOrden++);
            nuevosAnalisis.add(muestraAnalisis);
        }

        if (!nuevosAnalisis.isEmpty()) {
            muestraAnalisisRepository.saveAll(nuevosAnalisis);
        }

        return convertirADTO(muestra);
    }

    /**
     * Remover análisis de una muestra
     */
    public MuestraDTO removerAnalisis(UUID muestraId, UUID analisisId) {
        if (!muestraAnalisisRepository.existsByMuestraIdMuestraAndAnalisisIdAnalisis(muestraId, analisisId)) {
            throw new ResourceNotFoundException("La relación muestra-análisis no existe");
        }

        muestraAnalisisRepository.deleteByMuestraIdMuestraAndAnalisisIdAnalisis(muestraId, analisisId);

        Muestra muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId));

        return convertirADTO(muestra);
    }

    /**
     * Eliminar muestra
     */
    public void eliminarMuestra(UUID id) {
        Muestra muestra = muestraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + id));

        // Validación de integridad: No se puede eliminar si tiene tareas con resultados
        Long tareasConResultados = muestraAnalisisRepository.countTareasConResultadosByMuestra(id);
        if (tareasConResultados > 0) {
            throw new ValidationException(
                    String.format("No se puede eliminar la muestra. Tiene %d tarea(s) con resultados cargados (COMPLETADO o VALIDADO).",
                            tareasConResultados));
        }

        // Verificar que se puede eliminar (ej: no tiene análisis en proceso)
        boolean tieneAnalisisEnProceso = muestra.getMuestraAnalisis().stream()
                .anyMatch(ma -> ma.getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.EN_PROCESO);

        if (tieneAnalisisEnProceso) {
            throw new ValidationException("No se puede eliminar la muestra. Tiene análisis en proceso.");
        }

        muestraRepository.delete(muestra);
    }

    /**
     * Obtener muestras de un cliente
     */
    @Transactional(readOnly = true)
    public Page<MuestraDTO> obtenerMuestrasCliente(UUID clienteId, Pageable pageable) {
        Page<Muestra> muestras = muestraRepository.findByClienteIdCliente(clienteId, pageable);
        return muestras.map(this::convertirADTO);
    }

    /**
     * Obtener estadísticas de muestras
     */
    @Transactional(readOnly = true)
    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new HashMap<>();

        // Contar por estado
        for (Muestra.EstadoMuestra estado : Muestra.EstadoMuestra.values()) {
            Long count = muestraRepository.countByEstado(estado);
            estadisticas.put("total_" + estado.name().toLowerCase(), count);
        }

        // Muestras del día
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        Long muestrasHoy = muestraRepository.countMuestrasDesde(inicioHoy);
        estadisticas.put("muestras_hoy", muestrasHoy);

        // Muestras pendientes de alta prioridad
        List<Muestra> altaPrioridad = muestraRepository.findMuestrasAltaPrioridadPendientes();
        estadisticas.put("alta_prioridad_pendientes", altaPrioridad.size());

        return estadisticas;
    }

    /**
     * Obtener cola de trabajo para el laboratorio
     */
    @Transactional(readOnly = true)
    public List<MuestraDTO> obtenerColaLaboratorio(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        List<Muestra> muestras = muestraRepository.findMuestrasEnCola(pageable);
        return muestras.stream().map(this::convertirADTO).toList();
    }

    // Métodos privados auxiliares

    private void validarDatosCreacion(MuestraCreateDTO createDTO) {
        // Validar número interno único solo si fue proporcionado manualmente
        if (createDTO.getNumeroInterno() != null && !createDTO.getNumeroInterno().trim().isEmpty() &&
            muestraRepository.existsByNumeroInterno(createDTO.getNumeroInterno())) {
            throw new ValidationException("Ya existe una muestra con el número interno: " + createDTO.getNumeroInterno());
        }

        // Validar código de barras único si se proporciona
        if (createDTO.getCodigoBarras() != null && !createDTO.getCodigoBarras().trim().isEmpty() &&
            muestraRepository.existsByCodigoBarras(createDTO.getCodigoBarras())) {
            throw new ValidationException("Ya existe una muestra con el código de barras: " + createDTO.getCodigoBarras());
        }

        // Validar que se especifiquen análisis o plantillas
        if (!createDTO.tieneAnalisisOPlantillas()) {
            throw new ValidationException("Debe especificar al menos un análisis individual o una plantilla para la muestra");
        }
    }

    private void validarDatosActualizacion(MuestraCreateDTO updateDTO, UUID muestraId) {
        // Validar número interno único excluyendo la muestra actual
        if (muestraRepository.existsByNumeroInternoAndIdMuestraNotIn(
                updateDTO.getNumeroInterno(), Arrays.asList(muestraId))) {
            throw new ValidationException("Ya existe otra muestra con el número interno: " + updateDTO.getNumeroInterno());
        }

        // Validar código de barras único si se proporciona
        if (updateDTO.getCodigoBarras() != null && !updateDTO.getCodigoBarras().trim().isEmpty() &&
            muestraRepository.existsByCodigoBarrasAndIdMuestraNotIn(
                    updateDTO.getCodigoBarras(), Arrays.asList(muestraId))) {
            throw new ValidationException("Ya existe otra muestra con el código de barras: " + updateDTO.getCodigoBarras());
        }
    }

    private void mapearDatosCreacion(MuestraCreateDTO dto, Muestra muestra, Client cliente) {
        muestra.setNumeroInterno(dto.getNumeroInterno());
        muestra.setCodigoBarras(dto.getCodigoBarras());
        muestra.setCliente(cliente);
        muestra.setPuntoMuestreo(dto.getPuntoMuestreo());
        muestra.setTipoMuestra(dto.getTipoMuestra());
        
        Muestra.Prioridad prioridad = parsePrioridad(dto.getPrioridad());
        if (prioridad != null) {
            muestra.setPrioridad(prioridad);
        }

        Muestra.EstadoMuestra estado = parseEstado(dto.getEstado());
        if (estado != null) {
            muestra.setEstado(estado);
        }

        muestra.setFechaMuestreo(dto.getFechaMuestreo());
        muestra.setFechaRecepcion(dto.getFechaRecepcion());
        muestra.setResponsableMuestreo(dto.getResponsableMuestreo());
        muestra.setTemperaturaTransporte(dto.getTemperaturaTransporte());
        muestra.setCondicionesTransporte(dto.getCondicionesTransporte());
        muestra.setTipoEnvase(dto.getTipoEnvase());
        muestra.setConservantesUtilizados(dto.getConservantesUtilizados());
        muestra.setDescripcionConservantes(dto.getDescripcionConservantes());
        muestra.setObservaciones(dto.getObservaciones());
        muestra.setInfoCliente(dto.getInfoCliente());
        muestra.setMetodoAnalisis(dto.getMetodoAnalisis());
        muestra.setVolumenMuestra(dto.getVolumenMuestra());
        muestra.setUnidadVolumen(dto.getUnidadVolumen());
    }

    private void actualizarAnalisisMuestra(Muestra muestra, List<UUID> nuevosAnalisisIds) {
        // Obtener análisis actuales
        Set<UUID> analisisActuales = muestra.getMuestraAnalisis().stream()
                .map(ma -> ma.getAnalisis().getIdAnalisis())
                .collect(Collectors.toSet());

        Set<UUID> nuevosAnalisis = new HashSet<>(nuevosAnalisisIds);

        // Remover análisis que ya no están en la lista
        Set<UUID> paraRemover = new HashSet<>(analisisActuales);
        paraRemover.removeAll(nuevosAnalisis);
        
        for (UUID analisisId : paraRemover) {
            // Solo remover si no está en proceso o completado
            Optional<MuestraAnalisis> ma = muestraAnalisisRepository
                    .findByMuestraIdMuestraAndAnalisisIdAnalisis(muestra.getIdMuestra(), analisisId);
            
            if (ma.isPresent() && ma.get().getEstadoAnalisis() == MuestraAnalisis.EstadoAnalisis.PENDIENTE) {
                muestraAnalisisRepository.deleteByMuestraIdMuestraAndAnalisisIdAnalisis(
                        muestra.getIdMuestra(), analisisId);
            }
        }

        // Agregar nuevos análisis
        Set<UUID> paraAgregar = new HashSet<>(nuevosAnalisis);
        paraAgregar.removeAll(analisisActuales);
        
        if (!paraAgregar.isEmpty()) {
            agregarAnalisis(muestra.getIdMuestra(), new ArrayList<>(paraAgregar));
        }
    }

    private MuestraDTO convertirADTO(Muestra muestra) {
        MuestraDTO dto = new MuestraDTO();
        
        // Datos básicos
        dto.setIdMuestra(muestra.getIdMuestra());
        dto.setNumeroInterno(muestra.getNumeroInterno());
        dto.setCodigoBarras(muestra.getCodigoBarras());
        dto.setPuntoMuestreo(muestra.getPuntoMuestreo());
        dto.setTipoMuestra(muestra.getTipoMuestra());
        dto.setPrioridad(muestra.getPrioridad().name());
        dto.setEstado(muestra.getEstado().name());
        dto.setFechaMuestreo(muestra.getFechaMuestreo());
        dto.setFechaRecepcion(muestra.getFechaRecepcion());
        dto.setFechaCreacion(muestra.getFechaCreacion());
        dto.setFechaActualizacion(muestra.getFechaActualizacion());
        dto.setResponsableMuestreo(muestra.getResponsableMuestreo());
        dto.setTemperaturaTransporte(muestra.getTemperaturaTransporte());
        dto.setCondicionesTransporte(muestra.getCondicionesTransporte());
        dto.setTipoEnvase(muestra.getTipoEnvase());
        dto.setConservantesUtilizados(muestra.getConservantesUtilizados());
        dto.setDescripcionConservantes(muestra.getDescripcionConservantes());
        dto.setObservaciones(muestra.getObservaciones());
        dto.setInfoCliente(muestra.getInfoCliente());
        dto.setMetodoAnalisis(muestra.getMetodoAnalisis());
        dto.setVolumenMuestra(muestra.getVolumenMuestra());
        dto.setUnidadVolumen(muestra.getUnidadVolumen());

        // Cliente
        if (muestra.getCliente() != null) {
            MuestraDTO.ClienteBasicDTO clienteDTO = new MuestraDTO.ClienteBasicDTO(
                    muestra.getCliente().getIdCliente(),
                    muestra.getCliente().getNombreCliente(), // Usar nombreCliente
                    null, // RUT no disponible en la entidad Client
                    null  // Email no disponible en la entidad Client
            );
            dto.setCliente(clienteDTO);
        }

        // Análisis asociados
        if (muestra.getMuestraAnalisis() != null) {
            List<MuestraDTO.MuestraAnalisisDTO> analisisDTO = muestra.getMuestraAnalisis().stream()
                    .sorted(Comparator.comparing(MuestraAnalisis::getOrdenEjecucion))
                    .map(this::convertirMuestraAnalisisADTO)
                    .toList();
            
            dto.setAnalisis(analisisDTO);
            dto.setTotalAnalisis(analisisDTO.size());
            
            long completados = analisisDTO.stream()
                    .mapToLong(ma -> "COMPLETADO".equals(ma.getEstadoAnalisis()) ? 1 : 0)
                    .sum();
            
            dto.setAnalisisCompletados((int) completados);
            dto.setProgresoPorcentaje(!analisisDTO.isEmpty() ? 
                    (completados * 100.0) / analisisDTO.size() : 0.0);
        }

        return dto;
    }

    private MuestraDTO.MuestraAnalisisDTO convertirMuestraAnalisisADTO(MuestraAnalisis ma) {
        MuestraDTO.MuestraAnalisisDTO dto = new MuestraDTO.MuestraAnalisisDTO();
        
        dto.setIdMuestraAnalisis(ma.getIdMuestraAnalisis());
        dto.setIdAnalisis(ma.getAnalisis().getIdAnalisis());
        dto.setNombreAnalisis(ma.getAnalisis().getNombreAnalisis());
        dto.setEstadoAnalisis(ma.getEstadoAnalisis().name());
        dto.setOrdenEjecucion(ma.getOrdenEjecucion());
        dto.setFechaInicio(ma.getFechaInicio());
        dto.setFechaFinalizacion(ma.getFechaFinalizacion());
        dto.setTecnicoResponsable(ma.getTecnicoResponsable());
        dto.setObservacionesAnalisis(ma.getObservacionesAnalisis());
        dto.setCumpleNormativa(ma.getCumpleNormativa());
        dto.setEsControlCalidad(ma.getEsControlCalidad());
        dto.setNotasValidacion(ma.getNotasValidacion());
        
        return dto;
    }

    /**
     * Procesa análisis individuales para una muestra
     */
    private void procesarAnalisisIndividuales(Muestra muestra, List<UUID> analisisIds) {
        // Verificar que todos los análisis existen
        List<Analisis> analisisList = new ArrayList<>();
        for (UUID analisisId : analisisIds) {
            Analisis analisis = analisisRepository.findById(analisisId)
                    .orElseThrow(() -> new ResourceNotFoundException("Análisis no encontrado con ID: " + analisisId));
            analisisList.add(analisis);
        }

        // Crear las relaciones con los análisis
        List<MuestraAnalisis> muestraAnalisisList = new ArrayList<>();
        for (int i = 0; i < analisisList.size(); i++) {
            MuestraAnalisis muestraAnalisis = new MuestraAnalisis(muestra, analisisList.get(i), i + 1);
            muestraAnalisisList.add(muestraAnalisis);
        }
        
        muestraAnalisisRepository.saveAll(muestraAnalisisList);
    }

    /**
     * Procesa plantillas para una muestra, expandiendo automáticamente todos los análisis de cada plantilla
     */
    private void procesarPlantillas(Muestra muestra, List<UUID> plantillaIds) {
        // Verificar que todas las plantillas existen
        List<Plantilla> plantillasList = new ArrayList<>();
        for (UUID plantillaId : plantillaIds) {
            Plantilla plantilla = plantillaRepository.findById(plantillaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Plantilla no encontrada con ID: " + plantillaId));
            plantillasList.add(plantilla);
        }

        // Crear las relaciones muestra-plantilla
        List<MuestraPlantilla> muestraPlantillasList = new ArrayList<>();
        for (int i = 0; i < plantillasList.size(); i++) {
            MuestraPlantilla muestraPlantilla = new MuestraPlantilla(muestra, plantillasList.get(i), i + 1);
            muestraPlantillasList.add(muestraPlantilla);
        }
        
        muestraPlantillaRepository.saveAll(muestraPlantillasList);

        // Expandir automáticamente los análisis de cada plantilla
        expandirAnalisisDeBlantillas(muestra, plantillasList);
    }

    /**
     * Expande automáticamente todos los análisis de las plantillas en análisis individuales para la muestra
     */
    private void expandirAnalisisDeBlantillas(Muestra muestra, List<Plantilla> plantillas) {
        Set<UUID> analisisIdsUnicos = new HashSet<>();
        List<MuestraAnalisis> nuevosAnalisis = new ArrayList<>();
        
        // Obtener el siguiente orden disponible para análisis individuales
        int siguienteOrden = muestraAnalisisRepository.getSiguienteOrdenEjecucion(muestra.getIdMuestra());

        for (Plantilla plantilla : plantillas) {
            if (plantilla.getPlantillaAnalisis() != null) {
                for (PlantillaAnalisis plantillaAnalisis : plantilla.getPlantillaAnalisis()) {
                    UUID analisisId = plantillaAnalisis.getAnalisis().getIdAnalisis();
                    
                    // Evitar duplicados
                    if (!analisisIdsUnicos.contains(analisisId)) {
                        analisisIdsUnicos.add(analisisId);
                        
                        MuestraAnalisis muestraAnalisis = new MuestraAnalisis(
                            muestra, 
                            plantillaAnalisis.getAnalisis(), 
                            siguienteOrden++
                        );
                        muestraAnalisis.setObservacionesAnalisis("Generado automáticamente desde plantilla: " + plantilla.getNombrePlantilla());
                        nuevosAnalisis.add(muestraAnalisis);
                    }
                }
            }
        }

        if (!nuevosAnalisis.isEmpty()) {
            muestraAnalisisRepository.saveAll(nuevosAnalisis);
        }
    }

    /**
     * Agregar plantillas a una muestra existente
     */
    @Transactional
    public MuestraDTO agregarPlantillasAMuestra(UUID muestraId, List<UUID> plantillaIds) {
        Muestra muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId));

        if (plantillaIds != null && !plantillaIds.isEmpty()) {
            procesarPlantillas(muestra, plantillaIds);
        }

        // Recargar muestra con las nuevas relaciones
        muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId));

        return convertirADTO(muestra);
    }

    /**
     * Remover una plantilla específica de una muestra
     */
    @Transactional
    public MuestraDTO removerPlantillaDeMuestra(UUID muestraId, UUID plantillaId) {
        // Verificar que la muestra existe
        if (!muestraRepository.existsById(muestraId)) {
            throw new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId);
        }

        // Encontrar y eliminar la relación muestra-plantilla
        List<MuestraPlantilla> muestraPlantillas = muestraPlantillaRepository.findByMuestraId(muestraId);
        MuestraPlantilla plantillaAEliminar = muestraPlantillas.stream()
                .filter(mp -> mp.getPlantilla().getIdPlantilla().equals(plantillaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("La muestra no tiene asociada la plantilla con ID: " + plantillaId));

        muestraPlantillaRepository.delete(plantillaAEliminar);

        // Recargar muestra
        Muestra muestra = muestraRepository.findById(muestraId)
                .orElseThrow(() -> new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId));

        return convertirADTO(muestra);
    }

    /**
     * Obtener todas las plantillas asociadas a una muestra
     */
    @Transactional(readOnly = true)
    public List<MuestraPlantilla> obtenerPlantillasDeMuestra(UUID muestraId) {
        if (!muestraRepository.existsById(muestraId)) {
            throw new ResourceNotFoundException(MUESTRA_NO_ENCONTRADA + muestraId);
        }
        return muestraPlantillaRepository.findByMuestraId(muestraId);
    }

    /**
     * Actualizar estado de una plantilla específica en una muestra
     */
    @Transactional
    public MuestraPlantilla actualizarEstadoPlantilla(UUID muestraId, UUID plantillaId, 
                                                      MuestraPlantilla.EstadoPlantilla nuevoEstado) {
        MuestraPlantilla muestraPlantilla = muestraPlantillaRepository.findByMuestraId(muestraId)
                .stream()
                .filter(mp -> mp.getPlantilla().getIdPlantilla().equals(plantillaId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Plantilla no encontrada para la muestra"));

        muestraPlantilla.setEstadoPlantilla(nuevoEstado);
        
        if (nuevoEstado == MuestraPlantilla.EstadoPlantilla.EN_PROCESO && muestraPlantilla.getFechaInicio() == null) {
            muestraPlantilla.setFechaInicio(LocalDateTime.now());
        } else if (nuevoEstado == MuestraPlantilla.EstadoPlantilla.COMPLETADA && muestraPlantilla.getFechaFinalizacion() == null) {
            muestraPlantilla.setFechaFinalizacion(LocalDateTime.now());
        }

        return muestraPlantillaRepository.save(muestraPlantilla);
    }

    private Muestra.Prioridad parsePrioridad(String prioridadRaw) {
        if (prioridadRaw == null || prioridadRaw.isBlank()) {
            return null;
        }
        try {
            return Muestra.Prioridad.valueOf(prioridadRaw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("La prioridad debe ser ALTA, MEDIA o BAJA");
        }
    }

    private Muestra.EstadoMuestra parseEstado(String estadoRaw) {
        if (estadoRaw == null || estadoRaw.isBlank()) {
            return null;
        }
        try {
            return Muestra.EstadoMuestra.valueOf(estadoRaw.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("El estado debe ser RECIBIDA, EN_PROCESO, ANALIZADA, COMPLETADA o RECHAZADA");
        }
    }
}