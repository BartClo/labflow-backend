-- V23__Fix_muestra_analisis_estado_constraint.sql
-- Arreglar el CHECK constraint de estado_analisis para incluir VALIDADO

-- Eliminar el CHECK constraint existente para estado_analisis
ALTER TABLE muestra_analisis DROP CONSTRAINT IF EXISTS muestra_analisis_estado_analisis_check;

-- Recrear el CHECK constraint para estado_analisis incluyendo VALIDADO
ALTER TABLE muestra_analisis ADD CONSTRAINT muestra_analisis_estado_analisis_check 
    CHECK (estado_analisis IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'VALIDADO', 'CANCELADO'));

-- Actualizar valores nulos de estado_analisis a 'PENDIENTE' si existen
UPDATE muestra_analisis SET estado_analisis = 'PENDIENTE' WHERE estado_analisis IS NULL;

-- Comentario actualizado
COMMENT ON COLUMN muestra_analisis.estado_analisis IS 'Estado del análisis: PENDIENTE, EN_PROCESO, COMPLETADO, VALIDADO, CANCELADO';
