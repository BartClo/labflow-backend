package com.labflow.config;

import com.labflow.model.*;
import com.labflow.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);
    private static final String TIPO_EQUIPO = "EQUIPO";
    private static final String TIPO_REACTIVO = "REACTIVO";
    private static final String TIPO_INSUMO = "INSUMO";

    private final AnalisisRepository analisisRepo;
    private final ParametroRepository parametroRepo;
    private final EquipoRepository equipoRepo;
    private final ReactivoRepository reactivoRepo;
    private final InsumoRepository insumoRepo;
    private final AnalisisRecursoRepository analisisRecursoRepo;
    private final ConfigControlCalidadRepository configControlRepo;
    
    @PersistenceContext
    private EntityManager entityManager;

    public DataSeeder(AnalisisRepository analisisRepo, 
                      ParametroRepository parametroRepo,
                      EquipoRepository equipoRepo, 
                      ReactivoRepository reactivoRepo,
                      InsumoRepository insumoRepo, 
                      AnalisisRecursoRepository analisisRecursoRepo,
                      ConfigControlCalidadRepository configControlRepo) {
        this.analisisRepo = analisisRepo;
        this.parametroRepo = parametroRepo;
        this.equipoRepo = equipoRepo;
        this.reactivoRepo = reactivoRepo;
        this.insumoRepo = insumoRepo;
        this.analisisRecursoRepo = analisisRecursoRepo;
        this.configControlRepo = configControlRepo;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Evitar duplicados si ya existen datos
        if (analisisRecursoRepo.count() > 0 && configControlRepo.count() > 0) {
            log.info("ℹ️ La base de datos ya tiene datos cargados. Saltando Seeder...");
            return;
        }

        log.info("🚀 INICIANDO CARGA MASIVA DE DATOS...");
        cargarInventario();
        cargarReglasYLimites();
        log.info("✅ CARGA MASIVA FINALIZADA CON ÉXITO.");
    }

    private void cargarInventario() {
        try (InputStream is = getClass().getResourceAsStream("/data/Información laboratorio.xlsx")) {
            if (is == null) {
                log.warn("⚠️ Archivo de Inventario no encontrado en resources/data.");
                return;
            }
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);

                log.info("📂 Procesando Inventario y Recetas...");
            
                // La fila 8 tiene los encabezados, los datos empiezan en la fila 9 (índice 8)
                for (int i = 8; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    
                    if (row != null) {
                        // COLUMNA A: Nombre del Análisis
                        String nombreAnalisis = getCellValue(row.getCell(0));
                        
                        if (!nombreAnalisis.isEmpty() && !nombreAnalisis.toLowerCase().contains("método")) {
                            Optional<Analisis> analisisOpt = analisisRepo.findByNombre(nombreAnalisis);

                            // Si el análisis no existe, lo creamos temporalmente
                            Analisis analisis;
                            if (analisisOpt.isPresent()) {
                                analisis = analisisOpt.get();
                            } else {
                                Analisis nuevo = new Analisis();
                                nuevo.setNombreAnalisis(nombreAnalisis);
                                nuevo.setCodigo("GEN-" + System.currentTimeMillis());
                                nuevo.setCategoria("GENERAL");
                                analisis = analisisRepo.save(nuevo);
                                entityManager.flush(); // Asegurar que el análisis se persista antes de usarlo
                            }

                            // COLUMNA C (índice 2): Equipos | COLUMNA F (índice 5): Insumos | COLUMNA I (índice 8): Reactivos
                            procesarRecursoTexto(row.getCell(2), analisis, TIPO_EQUIPO);
                            procesarRecursoTexto(row.getCell(5), analisis, TIPO_INSUMO);
                            procesarRecursoTexto(row.getCell(8), analisis, TIPO_REACTIVO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("❌ Error al cargar inventario:", e);
        }
    }

    private void cargarReglasYLimites() {
        try (InputStream is = getClass().getResourceAsStream("/data/Controles LABCAUSS.xlsx")) {
            if (is == null) {
                log.warn("⚠️ Archivo de Controles no encontrado en resources/data.");
                return;
            }
            try (Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheet("Límites");
                if (sheet == null) {
                    sheet = workbook.getSheetAt(0);
                }

                log.info("📏 Procesando Límites Normativos y Criterios de Calidad...");

                // Obtener o crear un análisis genérico para parámetros sin análisis específico
                Analisis analisisGenerico = analisisRepo.findByNombre("Parámetros de Calidad")
                        .orElseGet(() -> {
                            Analisis nuevo = new Analisis();
                            nuevo.setNombreAnalisis("Parámetros de Calidad");
                            nuevo.setCodigo("PARAM-QC");
                            nuevo.setCategoria("CONTROL_CALIDAD");
                            nuevo.setEstado("Activo");
                            return analisisRepo.save(nuevo);
                        });

                int parametrosCreados = 0;
                int parametrosActualizados = 0;

                // Empezar en fila 2 (índice 1 = encabezados, índice 2 = datos)
                for (int i = 2; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    
                    if (row != null) {
                        // COLUMNA A: Analito (Parámetro)
                        String nombreParametro = getCellValue(row.getCell(0));
                        
                        if (!nombreParametro.isEmpty()) {
                            // COLUMNA B: Límite Normativo
                            String limiteTexto = getCellValue(row.getCell(1));

                            // COLUMNA C: Precisión (Duplicado)
                            String criterioPrecision = getCellValue(row.getCell(2));

                            // COLUMNA D: Exactitud (Estándar)
                            String criterioExactitud = getCellValue(row.getCell(3));

                            // COLUMNA E: Spike/Fortificada
                            String criterioSpike = getCellValue(row.getCell(4));

                            // COLUMNA F: Blanco
                            String criterioBlanco = getCellValue(row.getCell(5));

                            // Buscar o crear el parámetro
                            Optional<Parametro> parametroOpt = parametroRepo.findByNombre(nombreParametro);
                            Parametro parametro;

                            if (parametroOpt.isPresent()) {
                                // Actualizar parámetro existente
                                parametro = parametroOpt.get();
                                parametrosActualizados++;
                                log.debug("✓ Actualizando parámetro existente: {}", nombreParametro);
                            } else {
                                // Crear nuevo parámetro
                                parametro = new Parametro();
                                parametro.setNombre(nombreParametro);
                                parametro.setAnalisis(analisisGenerico);
                                parametro.setUnidad(""); // Se puede extraer del límite si es necesario
                                parametrosCreados++;
                                log.debug("✓ Creando nuevo parámetro: {}", nombreParametro);
                            }

                            // Establecer todos los criterios
                            parametro.setValorMaximoNormativa(limiteTexto);
                            parametro.setCriterioPrecision(criterioPrecision);
                            parametro.setCriterioExactitud(criterioExactitud);
                            parametro.setCriterioSpike(criterioSpike);
                            parametro.setCriterioBlanco(criterioBlanco);

                            parametroRepo.save(parametro);
                            entityManager.flush(); // Asegurar persistencia antes de crear reglas

                            // CREAR REGLAS DE CALIDAD (si aplican)
                            crearReglaCalidad(parametro, "Precision (Duplicado)", criterioPrecision);
                            crearReglaCalidad(parametro, "Exactitud (Control estandar)", criterioExactitud);
                            crearReglaCalidad(parametro, "Blanco", criterioBlanco);
                        }
                    }
                }

                log.info("✅ Límites y criterios de calidad procesados correctamente.");
                log.info("   📊 {} parámetros nuevos creados", parametrosCreados);
                log.info("   📊 {} parámetros actualizados", parametrosActualizados);
            }
        } catch (Exception e) {
            log.error("❌ Error al cargar límites y reglas:", e);
        }
    }

    private void procesarRecursoTexto(Cell cell, Analisis analisis, String tipo) {
        String texto = getCellValue(cell);
        if (texto.isEmpty()) return;

        String[] items = texto.split("[\n,•]");

        for (String itemRaw : items) {
            final String item = itemRaw.trim();
            
            if (item.length() >= 3) {
                AnalisisRecurso recurso = new AnalisisRecurso();
                recurso.setAnalisis(analisis);
                boolean recursoValido = true;

                switch (tipo) {
                    case TIPO_EQUIPO:
                        Equipo equipo = equipoRepo.findByNombre(item)
                                .orElseGet(() -> equipoRepo.save(new Equipo(item, "OPERATIVO")));
                        recurso.setEquipo(equipo);
                        break;
                    case TIPO_REACTIVO:
                        Reactivo reactivo = reactivoRepo.findByNombre(item)
                                .orElseGet(() -> reactivoRepo.save(new Reactivo(item, "LOTE-INIT", LocalDate.now().plusYears(1), BigDecimal.TEN)));
                        recurso.setReactivo(reactivo);
                        break;
                    case TIPO_INSUMO:
                        Insumo insumo = insumoRepo.findByNombre(item)
                                .orElseGet(() -> insumoRepo.save(new Insumo(item, BigDecimal.valueOf(100))));
                        recurso.setInsumo(insumo);
                        break;
                    default:
                        log.warn("Tipo de recurso desconocido: {}", tipo);
                        recursoValido = false;
                }
                
                if (recursoValido) {
                    analisisRecursoRepo.save(recurso);
                }
            }
        }
    }

    private void crearReglaCalidad(Parametro p, String tipo, String valorExcel) {
        if (valorExcel.isEmpty() || valorExcel.toLowerCase().contains("no procede") || valorExcel.toLowerCase().contains("no aplica")) return;
        
        ConfigControlCalidad regla = new ConfigControlCalidad();
        regla.setParametro(p);
        regla.setTipoControl(tipo);
        configControlRepo.save(regla);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA, BLANK, ERROR, _NONE -> "";
        };
    }
}
