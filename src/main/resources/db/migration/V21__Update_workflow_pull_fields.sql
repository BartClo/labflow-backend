-- V21: Update workflow pull fields
-- Adds support for pull-based workflow with task grouping

-- Add cumple_norma field to muestra_analisis
ALTER TABLE muestra_analisis
ADD COLUMN cumple_norma BOOLEAN;

-- Make codigo_barras NOT NULL in muestras (already has UNIQUE constraint)
-- First update any NULL values to a generated code (unlikely but safe)
UPDATE muestras
SET codigo_barras = 'CB-' || LPAD(numero_interno::TEXT, 8, '0')
WHERE codigo_barras IS NULL;

-- Now make it NOT NULL
ALTER TABLE muestras
ALTER COLUMN codigo_barras SET NOT NULL;

-- Add codigo_ot field to ordenes_trabajo with UNIQUE constraint
ALTER TABLE ordenes_trabajo
ADD COLUMN codigo_ot VARCHAR(50) UNIQUE;

-- Generate codigo_ot for existing orders
WITH numbered_ot AS (
    SELECT
        id_orden_trabajo,
        'OT-' ||
        TO_CHAR(fecha_creacion, 'YYYY') || '-' ||
        LPAD(
            ROW_NUMBER() OVER (
                PARTITION BY EXTRACT(YEAR FROM fecha_creacion)
                ORDER BY fecha_creacion
            )::TEXT,
            4,
            '0'
        ) AS new_codigo_ot
    FROM ordenes_trabajo
    WHERE codigo_ot IS NULL
)
UPDATE ordenes_trabajo ot
SET codigo_ot = n.new_codigo_ot
FROM numbered_ot n
WHERE ot.id_orden_trabajo = n.id_orden_trabajo;

-- Make codigo_ot NOT NULL after backfilling
ALTER TABLE ordenes_trabajo
ALTER COLUMN codigo_ot SET NOT NULL;

-- Create index on muestra_analisis for better performance on pending tasks queries
CREATE INDEX IF NOT EXISTS idx_muestra_analisis_ot_estado
ON muestra_analisis(orden_trabajo_id, estado_analisis);

-- Create index on codigo_barras for faster QR searches
CREATE INDEX IF NOT EXISTS idx_muestras_codigo_barras
ON muestras(codigo_barras);

-- Add comment to document the workflow
COMMENT ON COLUMN muestra_analisis.cumple_norma IS 'Indicates if the result meets NCh 409 normative limits';
COMMENT ON COLUMN ordenes_trabajo.codigo_ot IS 'Auto-generated work order code (OT-YYYY-NNNN)';
COMMENT ON INDEX idx_muestra_analisis_ot_estado IS 'Optimizes queries for pending tasks without work order';
