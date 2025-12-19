-- V18__Add_condiciones_transporte_to_muestras.sql
-- Agregar columna condiciones_transporte a la tabla muestras

ALTER TABLE muestras
ADD COLUMN IF NOT EXISTS condiciones_transporte VARCHAR(255);

COMMENT ON COLUMN muestras.condiciones_transporte IS 'Condiciones generales de transporte de la muestra';
