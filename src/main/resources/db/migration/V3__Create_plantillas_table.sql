-- ======================================================
-- Migración V3: Crear tabla plantillas
-- ======================================================
-- Esta migración crea la tabla plantillas para gestionar plantillas de procedimientos
-- que agrupan análisis de laboratorio

-- Habilitar extensión uuid-ossp si no está habilitada
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla plantillas
CREATE TABLE plantillas (
    id_plantilla UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre_plantilla VARCHAR(255) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50) NOT NULL DEFAULT 'Activo',
    
    -- Tipos de muestra aplicables (usando array)
    tipos_muestra_aplicables TEXT[],
    
    -- Campos de auditoría
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_plantilla_estado CHECK (estado IN ('Activo', 'Inactivo')),
    CONSTRAINT chk_plantilla_nombre_no_vacio CHECK (length(trim(nombre_plantilla)) > 0)
);

-- Crear tabla intermedia para la relación muchos a muchos entre plantillas y análisis
CREATE TABLE plantilla_analisis (
    id_plantilla_analisis UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    id_plantilla UUID NOT NULL,
    id_analisis UUID NOT NULL,
    orden_en_plantilla INTEGER NOT NULL DEFAULT 1,
    fecha_agregado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_plantilla_analisis_plantilla 
        FOREIGN KEY (id_plantilla) REFERENCES plantillas(id_plantilla) ON DELETE CASCADE,
    CONSTRAINT fk_plantilla_analisis_analisis 
        FOREIGN KEY (id_analisis) REFERENCES analisis(id_analisis) ON DELETE CASCADE,
    
    -- Constraint para evitar duplicados
    CONSTRAINT uk_plantilla_analisis 
        UNIQUE (id_plantilla, id_analisis),
    
    -- Constraint para orden positivo
    CONSTRAINT chk_orden_positivo CHECK (orden_en_plantilla > 0)
);

-- Crear índices para mejorar rendimiento
CREATE INDEX idx_plantillas_nombre ON plantillas(nombre_plantilla);
CREATE INDEX idx_plantillas_estado ON plantillas(estado);
CREATE INDEX idx_plantillas_tipos_muestra ON plantillas USING GIN(tipos_muestra_aplicables);

CREATE INDEX idx_plantilla_analisis_plantilla ON plantilla_analisis(id_plantilla);
CREATE INDEX idx_plantilla_analisis_analisis ON plantilla_analisis(id_analisis);
CREATE INDEX idx_plantilla_analisis_orden ON plantilla_analisis(id_plantilla, orden_en_plantilla);

-- Crear trigger para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION update_plantilla_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_plantilla_timestamp
    BEFORE UPDATE ON plantillas
    FOR EACH ROW
    EXECUTE FUNCTION update_plantilla_timestamp();

-- Insertar datos de ejemplo
INSERT INTO plantillas (
    nombre_plantilla, 
    descripcion, 
    estado, 
    tipos_muestra_aplicables
) VALUES 
(
    'Análisis Completo de Agua Potable',
    'Plantilla estándar para análisis completo de calidad de agua potable según normativas chilenas',
    'Activo',
    ARRAY['Agua']
),
(
    'Análisis Microbiológico Estándar',
    'Plantilla para análisis microbiológicos en diferentes tipos de muestras',
    'Activo',
    ARRAY['Agua', 'Alimentos', 'Otros']
),
(
    'Control de Calidad de Suelos',
    'Plantilla para análisis fisicoquímico completo de suelos agrícolas e industriales',
    'Activo',
    ARRAY['Suelo']
),
(
    'Análisis de Calidad del Aire',
    'Plantilla para monitoreo de contaminantes atmosféricos',
    'Activo',
    ARRAY['Aire']
);

-- Insertar relaciones de ejemplo (asumiendo que existen análisis en la tabla analisis)
-- Nota: Estos inserts solo funcionarán si ya existen análisis con esos códigos
INSERT INTO plantilla_analisis (id_plantilla, id_analisis, orden_en_plantilla)
SELECT 
    p.id_plantilla,
    a.id_analisis,
    1
FROM plantillas p, analisis a 
WHERE p.nombre_plantilla = 'Análisis Completo de Agua Potable' 
  AND a.codigo IN ('PH-001', 'TURB-001')
  AND EXISTS (SELECT 1 FROM analisis WHERE codigo = 'PH-001')
  AND EXISTS (SELECT 1 FROM analisis WHERE codigo = 'TURB-001');

-- Comentarios de la migración
COMMENT ON TABLE plantillas IS 'Tabla para almacenar plantillas de procedimientos que agrupan análisis de laboratorio';
COMMENT ON TABLE plantilla_analisis IS 'Tabla intermedia para la relación muchos a muchos entre plantillas y análisis';

COMMENT ON COLUMN plantillas.nombre_plantilla IS 'Nombre descriptivo de la plantilla';
COMMENT ON COLUMN plantillas.descripcion IS 'Descripción detallada del propósito y uso de la plantilla';
COMMENT ON COLUMN plantillas.estado IS 'Estado actual de la plantilla (Activo, Inactivo)';
COMMENT ON COLUMN plantillas.tipos_muestra_aplicables IS 'Array de tipos de muestra donde aplica esta plantilla';

COMMENT ON COLUMN plantilla_analisis.orden_en_plantilla IS 'Orden de ejecución del análisis dentro de la plantilla';
COMMENT ON COLUMN plantilla_analisis.fecha_agregado IS 'Fecha cuando se agregó el análisis a la plantilla';