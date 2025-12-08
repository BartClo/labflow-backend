-- ======================================================
-- Migración V8: Crear tablas parametro y config_control_calidad
-- ======================================================
-- Esta migración crea las tablas para gestionar parámetros de análisis
-- y sus configuraciones de control de calidad según NCh 409

-- Crear tabla parametro
CREATE TABLE parametro (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    analisis_id UUID NOT NULL,
    nombre VARCHAR(255) NOT NULL,
    unidad VARCHAR(50),
    valor_maximo_normativa DECIMAL(15,6),
    
    -- Campos de auditoría
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    CONSTRAINT fk_parametro_analisis 
        FOREIGN KEY (analisis_id) 
        REFERENCES analisis(id_analisis) 
        ON DELETE CASCADE,
    
    -- Unique constraint para evitar duplicados
    CONSTRAINT uk_parametro_analisis_nombre 
        UNIQUE (analisis_id, nombre)
);

-- Crear tabla config_control_calidad
CREATE TABLE config_control_calidad (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    parametro_id UUID NOT NULL,
    tipo_control VARCHAR(100) NOT NULL,
    recuperacion_min DECIMAL(5,2),
    recuperacion_max DECIMAL(5,2),
    
    -- Campos de auditoría
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraint
    CONSTRAINT fk_config_parametro 
        FOREIGN KEY (parametro_id) 
        REFERENCES parametro(id) 
        ON DELETE CASCADE,
    
    -- Constraints para validar rangos (permite valores sobre 100% para casos como Spike)
    CONSTRAINT chk_config_recuperacion_min 
        CHECK (recuperacion_min >= 0 AND recuperacion_min <= 200),
    CONSTRAINT chk_config_recuperacion_max 
        CHECK (recuperacion_max >= 0 AND recuperacion_max <= 200),
    CONSTRAINT chk_config_recuperacion_range 
        CHECK (recuperacion_max >= recuperacion_min),
    
    -- Tipos de control válidos según los datos HTML
    CONSTRAINT chk_config_tipo_control 
        CHECK (tipo_control IN (
            'Precision (Duplicado)',
            'Exactitud (Control estandar)',
            'Fortificada/Spike',
            'Blanco'
        ))
);

-- Crear índices
CREATE INDEX idx_parametro_analisis ON parametro(analisis_id);
CREATE INDEX idx_parametro_nombre ON parametro(nombre);
CREATE INDEX idx_config_parametro ON config_control_calidad(parametro_id);
CREATE INDEX idx_config_tipo ON config_control_calidad(tipo_control);

-- Crear triggers para actualizar timestamps automáticamente
CREATE OR REPLACE FUNCTION update_parametro_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_parametro_timestamp
    BEFORE UPDATE ON parametro
    FOR EACH ROW
    EXECUTE FUNCTION update_parametro_timestamp();

CREATE OR REPLACE FUNCTION update_config_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_config_timestamp
    BEFORE UPDATE ON config_control_calidad
    FOR EACH ROW
    EXECUTE FUNCTION update_config_timestamp();

-- Comentarios en las tablas
COMMENT ON TABLE parametro IS 'Parámetros que se miden en cada análisis de laboratorio';
COMMENT ON COLUMN parametro.id IS 'Identificador único del parámetro';
COMMENT ON COLUMN parametro.analisis_id IS 'Referencia al análisis al que pertenece este parámetro';
COMMENT ON COLUMN parametro.nombre IS 'Nombre del parámetro (ej: Turbiedad, Cobre, pH, etc.)';
COMMENT ON COLUMN parametro.unidad IS 'Unidad de medida del parámetro (ej: UNT, mg/L, unidades de pH)';
COMMENT ON COLUMN parametro.valor_maximo_normativa IS 'Valor máximo permitido según NCh 409';

COMMENT ON TABLE config_control_calidad IS 'Configuración de controles de calidad para cada parámetro según tipo de control';
COMMENT ON COLUMN config_control_calidad.id IS 'Identificador único de la configuración';
COMMENT ON COLUMN config_control_calidad.parametro_id IS 'Referencia al parámetro';
COMMENT ON COLUMN config_control_calidad.tipo_control IS 'Tipo de control de calidad a aplicar';
COMMENT ON COLUMN config_control_calidad.recuperacion_min IS 'Porcentaje mínimo de recuperación aceptable';
COMMENT ON COLUMN config_control_calidad.recuperacion_max IS 'Porcentaje máximo de recuperación aceptable';

-- Insertar datos de ejemplo basados en el archivo HTML "Controles LABCAUSS"
-- Para el análisis de Turbiedad
DO $$
DECLARE
    v_turbiedad_analisis_id UUID;
    v_turbiedad_param_id UUID;
BEGIN
    -- Obtener el ID del análisis de Turbiedad si existe
    SELECT id_analisis INTO v_turbiedad_analisis_id 
    FROM analisis 
    WHERE codigo = 'AN-001' OR nombre_analisis ILIKE '%pH%'
    LIMIT 1;
    
    IF v_turbiedad_analisis_id IS NOT NULL THEN
        -- Insertar parámetro pH
        INSERT INTO parametro (analisis_id, nombre, unidad, valor_maximo_normativa)
        VALUES (v_turbiedad_analisis_id, 'pH', 'unidades de pH', 8.5)
        RETURNING id INTO v_turbiedad_param_id;
        
        -- Insertar configuraciones de control de calidad para pH
        INSERT INTO config_control_calidad (parametro_id, tipo_control, recuperacion_min, recuperacion_max)
        VALUES 
            (v_turbiedad_param_id, 'Precision (Duplicado)', 90.0, 100.0),
            (v_turbiedad_param_id, 'Exactitud (Control estandar)', 90.0, 110.0),
            (v_turbiedad_param_id, 'Fortificada/Spike', 80.0, 120.0);
    END IF;
END $$;

-- Insertar más ejemplos de parámetros comunes
DO $$
DECLARE
    v_analisis_id UUID;
    v_parametro_id UUID;
BEGIN
    -- Buscar cualquier análisis existente para agregar parámetros de ejemplo
    SELECT id_analisis INTO v_analisis_id 
    FROM analisis 
    LIMIT 1;
    
    IF v_analisis_id IS NOT NULL THEN
        -- Turbiedad
        INSERT INTO parametro (analisis_id, nombre, unidad, valor_maximo_normativa)
        VALUES (v_analisis_id, 'Turbiedad', 'UNT', 4.0)
        RETURNING id INTO v_parametro_id;
        
        INSERT INTO config_control_calidad (parametro_id, tipo_control, recuperacion_min, recuperacion_max)
        VALUES 
            (v_parametro_id, 'Precision (Duplicado)', 90.0, 100.0),
            (v_parametro_id, 'Exactitud (Control estandar)', 90.0, 110.0),
            (v_parametro_id, 'Fortificada/Spike', 80.0, 120.0);
    END IF;
END $$;
