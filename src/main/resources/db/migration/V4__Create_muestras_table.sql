-- V4__Create_muestras_table.sql
-- Migración para crear tabla de muestras y relación con análisis

-- Crear tabla de muestras
CREATE TABLE muestras (
    id_muestra UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    numero_interno VARCHAR(50) UNIQUE NOT NULL,
    codigo_barras VARCHAR(100) UNIQUE,
    
    -- Información del cliente
    id_cliente UUID NOT NULL,
    punto_muestreo VARCHAR(255),
    
    -- Tipo y características de la muestra
    tipo_muestra VARCHAR(100) NOT NULL,
    prioridad VARCHAR(20) DEFAULT 'MEDIA' CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA')),
    
    -- Condiciones de transporte
    temperatura_transporte DECIMAL(5,2),
    tipo_contenedor VARCHAR(100),
    metodo_preservacion VARCHAR(255),
    
    -- Fechas y recepción
    fecha_recepcion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_muestreo TIMESTAMP,
    recibida_por VARCHAR(255),
    
    -- Estado de la muestra
    estado VARCHAR(50) DEFAULT 'RECIBIDA' CHECK (estado IN ('RECIBIDA', 'EN_PROCESO', 'ANALIZADA', 'COMPLETADA', 'RECHAZADA')),
    
    -- Observaciones
    observaciones TEXT,
    
    -- Metadatos
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key
    CONSTRAINT fk_muestra_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente) ON DELETE RESTRICT
);

-- Crear tabla de relación many-to-many entre muestras y análisis
CREATE TABLE muestra_analisis (
    id_muestra_analisis UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_muestra UUID NOT NULL,
    id_analisis UUID NOT NULL,
    
    -- Estado específico del análisis para esta muestra
    estado_analisis VARCHAR(50) DEFAULT 'PENDIENTE' CHECK (estado_analisis IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'CANCELADO')),
    
    -- Orden de ejecución de los análisis
    orden_ejecucion INTEGER DEFAULT 1,
    
    -- Resultados y fechas
    resultado JSONB,
    fecha_inicio TIMESTAMP,
    fecha_finalizacion TIMESTAMP,
    tecnico_responsable VARCHAR(255),
    
    -- Observaciones específicas del análisis
    observaciones_analisis TEXT,
    
    -- Metadatos
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_muestra_analisis_muestra FOREIGN KEY (id_muestra) REFERENCES muestras(id_muestra) ON DELETE CASCADE,
    CONSTRAINT fk_muestra_analisis_analisis FOREIGN KEY (id_analisis) REFERENCES analisis(id_analisis) ON DELETE RESTRICT,
    
    -- Constraint único para evitar duplicados
    CONSTRAINT unique_muestra_analisis UNIQUE (id_muestra, id_analisis)
);

-- Crear índices para mejorar performance
CREATE INDEX idx_muestras_numero_interno ON muestras(numero_interno);
CREATE INDEX idx_muestras_codigo_barras ON muestras(codigo_barras);
CREATE INDEX idx_muestras_cliente ON muestras(id_cliente);
CREATE INDEX idx_muestras_tipo ON muestras(tipo_muestra);
CREATE INDEX idx_muestras_estado ON muestras(estado);
CREATE INDEX idx_muestras_fecha_recepcion ON muestras(fecha_recepcion);

CREATE INDEX idx_muestra_analisis_muestra ON muestra_analisis(id_muestra);
CREATE INDEX idx_muestra_analisis_analisis ON muestra_analisis(id_analisis);
CREATE INDEX idx_muestra_analisis_estado ON muestra_analisis(estado_analisis);

-- Crear trigger para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION update_muestras_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_muestras_updated_at
    BEFORE UPDATE ON muestras
    FOR EACH ROW
    EXECUTE FUNCTION update_muestras_updated_at();

CREATE OR REPLACE FUNCTION update_muestra_analisis_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER trigger_muestra_analisis_updated_at
    BEFORE UPDATE ON muestra_analisis
    FOR EACH ROW
    EXECUTE FUNCTION update_muestra_analisis_updated_at();

-- Insertar datos de ejemplo (comentado temporalmente para corregir referencias)
/*
INSERT INTO muestras (
    numero_interno, codigo_barras, id_cliente, punto_muestreo, tipo_muestra, prioridad,
    temperatura_transporte, tipo_contenedor, metodo_preservacion, 
    fecha_muestreo, recibida_por, observaciones
) VALUES 
(
    'MU-2025001', 
    'BC001234567890',
    (SELECT id_cliente FROM clientes LIMIT 1),
    'Grifo Principal - Entrada',
    'Agua Potable',
    'Normal',
    4.0,
    'Frasco Estéril 500ml',
    'Refrigeración 2-8°C',
    CURRENT_TIMESTAMP - INTERVAL '2 hours',
    'Juan Pérez',
    'Muestra tomada en condiciones normales'
),
(
    'MU-2025002',
    'BC001234567891', 
    (SELECT id_cliente FROM clientes LIMIT 1),
    'Pozo de Monitoreo PM-1',
    'Agua Subterránea',
    'Alta',
    4.0,
    'Frasco Ámbar 1L',
    'HNO3 + Refrigeración',
    CURRENT_TIMESTAMP - INTERVAL '6 hours',
    'María González',
    'Muestra para análisis de metales pesados'
);
*/

-- Asignar análisis a las muestras de ejemplo (comentado temporalmente)
/*
INSERT INTO muestra_analisis (id_muestra, id_analisis, orden_ejecucion, tecnico_responsable)
SELECT 
    m.id_muestra,
    a.id_analisis,
    ROW_NUMBER() OVER (PARTITION BY m.id_muestra ORDER BY a.nombre_analisis),
    'Técnico Lab'
FROM muestras m
CROSS JOIN analisis a
WHERE m.numero_interno IN ('MU-2025001', 'MU-2025002')
AND a.estado = 'Activo'
LIMIT 6; -- Limitar a 3 análisis por muestra
*/

-- Crear tabla de relación many-to-many entre muestras y plantillas
CREATE TABLE muestra_plantilla (
    id_muestra_plantilla UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_muestra UUID NOT NULL,
    id_plantilla UUID NOT NULL,
    
    -- Estado específico de la plantilla para esta muestra
    estado_plantilla VARCHAR(50) DEFAULT 'PENDIENTE' CHECK (estado_plantilla IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA')),
    
    -- Orden de ejecución de las plantillas
    orden_ejecucion INTEGER DEFAULT 1 CHECK (orden_ejecucion > 0),
    
    -- Fechas de ejecución
    fecha_inicio TIMESTAMP,
    fecha_finalizacion TIMESTAMP,
    
    -- Técnico responsable
    tecnico_responsable VARCHAR(255),
    
    -- Observaciones específicas de la plantilla
    observaciones_plantilla TEXT,
    
    -- Metadatos
    fecha_agregada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_muestra_plantilla_muestra FOREIGN KEY (id_muestra) REFERENCES muestras(id_muestra) ON DELETE CASCADE,
    CONSTRAINT fk_muestra_plantilla_plantilla FOREIGN KEY (id_plantilla) REFERENCES plantillas(id_plantilla) ON DELETE RESTRICT,
    
    -- Constraint de unicidad para evitar duplicados
    CONSTRAINT uq_muestra_plantilla UNIQUE (id_muestra, id_plantilla)
);

-- Índices adicionales para plantillas
CREATE INDEX idx_muestra_plantilla_muestra ON muestra_plantilla(id_muestra);
CREATE INDEX idx_muestra_plantilla_plantilla ON muestra_plantilla(id_plantilla);
CREATE INDEX idx_muestra_plantilla_estado ON muestra_plantilla(estado_plantilla);
CREATE INDEX idx_muestra_plantilla_orden ON muestra_plantilla(orden_ejecucion);

-- Comentarios de documentación
COMMENT ON TABLE muestras IS 'Tabla principal de muestras del laboratorio';
COMMENT ON TABLE muestra_analisis IS 'Relación many-to-many entre muestras y análisis solicitados';
COMMENT ON TABLE muestra_plantilla IS 'Relación many-to-many entre muestras y plantillas de procedimientos';
COMMENT ON COLUMN muestras.numero_interno IS 'Número interno único de la muestra generado por el sistema';
COMMENT ON COLUMN muestras.codigo_barras IS 'Código de barras de la muestra para identificación rápida';
COMMENT ON COLUMN muestras.estado IS 'Estado actual de la muestra en el flujo de trabajo del laboratorio';
COMMENT ON COLUMN muestra_analisis.estado_analisis IS 'Estado específico del análisis para esta muestra';
COMMENT ON COLUMN muestra_analisis.resultado IS 'Resultados del análisis en formato JSON';
COMMENT ON COLUMN muestra_plantilla.estado_plantilla IS 'Estado específico de la plantilla para esta muestra';