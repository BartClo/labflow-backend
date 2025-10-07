-- ======================================================
-- Migración V2: Crear tabla analisis
-- ======================================================
-- Esta migración crea la tabla analisis para gestionar análisis de laboratorio

-- Habilitar extensión uuid-ossp si no está habilitada
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla analisis
CREATE TABLE analisis (
    id_analisis UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre_analisis VARCHAR(255) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    categoria VARCHAR(100) NOT NULL,
    metodo_ensayo VARCHAR(255),
    
    -- Tipos de muestra aplicables (usando array)
    tipos_muestra_aplicables TEXT[],
    
    -- Parámetros a medir (JSON para flexibilidad)
    parametros_medir JSONB,
    
    -- Equipos requeridos (JSON para flexibilidad)
    equipos_requeridos JSONB,
    
    -- Duración estimada en horas
    duracion_estimada_horas DECIMAL(5,2),
    
    -- Precio en CLP
    precio_clp DECIMAL(10,2),
    
    -- Estado del análisis
    estado VARCHAR(50) NOT NULL DEFAULT 'Activo',
    
    -- Campos de auditoría
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    -- Constraints
    CONSTRAINT chk_analisis_estado CHECK (estado IN ('Activo', 'Inactivo', 'En Desarrollo')),
    CONSTRAINT chk_analisis_duracion CHECK (duracion_estimada_horas > 0),
    CONSTRAINT chk_analisis_precio CHECK (precio_clp >= 0)
);

-- Crear índices para mejorar rendimiento
CREATE INDEX idx_analisis_codigo ON analisis(codigo);
CREATE INDEX idx_analisis_categoria ON analisis(categoria);
CREATE INDEX idx_analisis_estado ON analisis(estado);
CREATE INDEX idx_analisis_nombre ON analisis(nombre_analisis);

-- Crear trigger para actualizar fecha_actualizacion automáticamente
CREATE OR REPLACE FUNCTION update_analisis_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_analisis_timestamp
    BEFORE UPDATE ON analisis
    FOR EACH ROW
    EXECUTE FUNCTION update_analisis_timestamp();

-- Insertar algunos análisis de ejemplo
INSERT INTO analisis (
    nombre_analisis, 
    codigo, 
    descripcion, 
    categoria, 
    metodo_ensayo,
    tipos_muestra_aplicables,
    parametros_medir,
    equipos_requeridos,
    duracion_estimada_horas,
    precio_clp
) VALUES 
(
    'Determinación de pH',
    'AN-001',
    'Determinación del pH en muestras líquidas y sólidas',
    'Físico-Químico',
    'NCh 409/1, APHA 4500',
    ARRAY['Agua', 'Suelo', 'Alimentos'],
    '{"parametros": [{"nombre": "pH", "unidad": "unidades de pH", "limite_deteccion": 0.1, "limite_maximo": 14}]}',
    '{"equipos": [{"nombre": "pHmetro", "duracion_horas": 2, "precio_clp": 15000, "estado": "Activo"}]}',
    2.5,
    25000
),
(
    'Metales Pesados',
    'ICP-OES-001',
    'Determinación de metales pesados por ICP-OES',
    'Metales',
    'EPA 200.7',
    ARRAY['Agua', 'Suelo'],
    '{"parametros": [{"nombre": "Plomo", "unidad": "mg/L", "limite_deteccion": 0.01}, {"nombre": "Cadmio", "unidad": "mg/L", "limite_deteccion": 0.005}]}',
    '{"equipos": [{"nombre": "ICP-OES", "duracion_horas": 4, "precio_clp": 45000, "estado": "Activo"}]}',
    6.0,
    85000
),
(
    'Coliformes Totales',
    'NMP-001',
    'Recuento de coliformes totales por NMP',
    'Microbiológico',
    'Filtración por membrana',
    ARRAY['Agua'],
    '{"parametros": [{"nombre": "Coliformes", "unidad": "NMP/100mL", "limite_deteccion": 1}]}',
    '{"equipos": [{"nombre": "Incubadora", "duracion_horas": 24, "precio_clp": 25000, "estado": "Activo"}]}',
    24.0,
    35000
);

-- Comentarios en la tabla
COMMENT ON TABLE analisis IS 'Tabla para gestionar análisis de laboratorio';
COMMENT ON COLUMN analisis.id_analisis IS 'Identificador único del análisis';
COMMENT ON COLUMN analisis.codigo IS 'Código único del análisis';
COMMENT ON COLUMN analisis.tipos_muestra_aplicables IS 'Array de tipos de muestra donde se puede aplicar el análisis';
COMMENT ON COLUMN analisis.parametros_medir IS 'JSON con parámetros a medir y sus características';
COMMENT ON COLUMN analisis.equipos_requeridos IS 'JSON con equipos necesarios para el análisis';