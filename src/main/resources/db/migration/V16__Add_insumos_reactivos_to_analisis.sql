-- Migration to add insumos and reactivos fields to analisis table
-- Version: V9__Add_insumos_reactivos_to_analisis.sql

-- Add new columns for insumos and reactivos
ALTER TABLE analisis 
ADD COLUMN IF NOT EXISTS insumos_requeridos JSONB,
ADD COLUMN IF NOT EXISTS reactivos_requeridos JSONB;

-- Add comments for better documentation
COMMENT ON COLUMN analisis.insumos_requeridos IS 'Insumos requeridos para el análisis en formato JSON';
COMMENT ON COLUMN analisis.reactivos_requeridos IS 'Reactivos requeridos para el análisis en formato JSON';

-- Create indexes for performance on JSON fields if needed
CREATE INDEX IF NOT EXISTS idx_analisis_insumos_requeridos ON analisis USING GIN (insumos_requeridos);
CREATE INDEX IF NOT EXISTS idx_analisis_reactivos_requeridos ON analisis USING GIN (reactivos_requeridos);