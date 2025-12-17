-- V17__Make_numero_interno_nullable.sql
-- Migración para permitir que numero_interno sea NULL temporalmente
-- antes de que el sistema lo genere automáticamente

-- Modificar la columna para permitir valores NULL
ALTER TABLE muestras 
ALTER COLUMN numero_interno DROP NOT NULL;

-- Actualizar el comentario de la columna
COMMENT ON COLUMN muestras.numero_interno IS 'Número interno único de la muestra generado automáticamente por el sistema con formato M-AAAA-XXXX';
