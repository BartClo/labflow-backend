-- Agregar campos de validación y control de calidad a muestra_analisis

ALTER TABLE muestra_analisis
ADD COLUMN cumple_normativa BOOLEAN,
ADD COLUMN es_control_calidad BOOLEAN DEFAULT FALSE,
ADD COLUMN notas_validacion TEXT;

-- Crear índice para búsquedas de control de calidad
CREATE INDEX idx_muestra_analisis_control_calidad ON muestra_analisis(es_control_calidad);
CREATE INDEX idx_muestra_analisis_cumple_normativa ON muestra_analisis(cumple_normativa);

-- Comentarios para documentación
COMMENT ON COLUMN muestra_analisis.cumple_normativa IS 'Indica si el análisis cumple con la normativa aplicable';
COMMENT ON COLUMN muestra_analisis.es_control_calidad IS 'Marca el análisis como control de calidad';
COMMENT ON COLUMN muestra_analisis.notas_validacion IS 'Notas del proceso de validación del análisis';
