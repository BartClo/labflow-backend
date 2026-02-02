-- V22__Fix_muestras_table_structure.sql
-- Sincronizar estructura de tablas muestras y muestra_analisis con los modelos Java

-- =====================================================
-- FIX TABLA MUESTRAS
-- =====================================================

-- Eliminar el CHECK constraint existente para estado (si existe)
ALTER TABLE muestras DROP CONSTRAINT IF EXISTS muestras_estado_check;

-- Recrear el CHECK constraint para estado con los valores correctos en mayúsculas
ALTER TABLE muestras ADD CONSTRAINT muestras_estado_check 
    CHECK (estado IN ('RECIBIDA', 'EN_PROCESO', 'ANALIZADA', 'COMPLETADA', 'RECHAZADA'));

-- Eliminar el CHECK constraint existente para prioridad (si existe)  
ALTER TABLE muestras DROP CONSTRAINT IF EXISTS muestras_prioridad_check;

-- Recrear el CHECK constraint para prioridad
ALTER TABLE muestras ADD CONSTRAINT muestras_prioridad_check 
    CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA'));

-- Asegurar que las columnas adicionales existen y tienen los tipos correctos
ALTER TABLE muestras 
    ALTER COLUMN numero_interno DROP NOT NULL,
    ALTER COLUMN tipo_muestra DROP NOT NULL;

-- Actualizar valores nulos de estado a 'RECIBIDA' si existen
UPDATE muestras SET estado = 'RECIBIDA' WHERE estado IS NULL;

-- Actualizar valores nulos de prioridad a 'MEDIA' si existen  
UPDATE muestras SET prioridad = 'MEDIA' WHERE prioridad IS NULL;

-- Comentarios actualizados
COMMENT ON COLUMN muestras.estado IS 'Estado de la muestra: RECIBIDA, EN_PROCESO, ANALIZADA, COMPLETADA, RECHAZADA';
COMMENT ON COLUMN muestras.prioridad IS 'Prioridad de la muestra: BAJA, MEDIA, ALTA';

-- =====================================================
-- FIX TABLA MUESTRA_ANALISIS
-- =====================================================

-- Eliminar el CHECK constraint existente para estado_analisis
ALTER TABLE muestra_analisis DROP CONSTRAINT IF EXISTS muestra_analisis_estado_analisis_check;

-- Recrear el CHECK constraint para estado_analisis incluyendo VALIDADO
ALTER TABLE muestra_analisis ADD CONSTRAINT muestra_analisis_estado_analisis_check 
    CHECK (estado_analisis IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADO', 'VALIDADO', 'CANCELADO'));

-- Actualizar valores nulos de estado_analisis a 'PENDIENTE' si existen
UPDATE muestra_analisis SET estado_analisis = 'PENDIENTE' WHERE estado_analisis IS NULL;

-- Comentario actualizado
COMMENT ON COLUMN muestra_analisis.estado_analisis IS 'Estado del análisis: PENDIENTE, EN_PROCESO, COMPLETADO, VALIDADO, CANCELADO';
