-- V19__Add_missing_columns_to_muestras.sql
-- Agregar columnas faltantes a la tabla muestras

ALTER TABLE muestras
ADD COLUMN IF NOT EXISTS responsable_muestreo VARCHAR(255),
ADD COLUMN IF NOT EXISTS tipo_envase VARCHAR(100),
ADD COLUMN IF NOT EXISTS conservantes_utilizados BOOLEAN DEFAULT false,
ADD COLUMN IF NOT EXISTS descripcion_conservantes VARCHAR(255),
ADD COLUMN IF NOT EXISTS info_cliente VARCHAR(255),
ADD COLUMN IF NOT EXISTS metodo_analisis VARCHAR(255),
ADD COLUMN IF NOT EXISTS volumen_muestra DECIMAL(10,3),
ADD COLUMN IF NOT EXISTS unidad_volumen VARCHAR(50);

-- Agregar comentarios descriptivos
COMMENT ON COLUMN muestras.responsable_muestreo IS 'Persona responsable del muestreo';
COMMENT ON COLUMN muestras.tipo_envase IS 'Tipo de envase utilizado para la muestra';
COMMENT ON COLUMN muestras.conservantes_utilizados IS 'Indica si se utilizaron conservantes';
COMMENT ON COLUMN muestras.descripcion_conservantes IS 'Descripción de los conservantes utilizados';
COMMENT ON COLUMN muestras.info_cliente IS 'Información adicional del cliente';
COMMENT ON COLUMN muestras.metodo_analisis IS 'Método de análisis a aplicar';
COMMENT ON COLUMN muestras.volumen_muestra IS 'Volumen de la muestra';
COMMENT ON COLUMN muestras.unidad_volumen IS 'Unidad de medida del volumen (mL, L, etc.)';
