-- ======================================================
-- Migración V7: Ajustar tabla analisis para compatibilidad con parametros
-- ======================================================
-- Esta migración ajusta la tabla analisis para preparar la relación con parametros

-- Agregar columna dias_entrega (según diagrama)
ALTER TABLE analisis
ADD COLUMN IF NOT EXISTS dias_entrega INTEGER DEFAULT 1;

-- Agregar constraint para dias_entrega
ALTER TABLE analisis
ADD CONSTRAINT chk_analisis_dias_entrega CHECK (dias_entrega > 0);

-- Agregar comentarios
COMMENT ON COLUMN analisis.dias_entrega IS 'Días estimados para la entrega de resultados del análisis';
COMMENT ON COLUMN analisis.metodo_ensayo IS 'Método de referencia utilizado para el análisis';
COMMENT ON COLUMN analisis.precio_clp IS 'Precio base del análisis en pesos chilenos';

-- Actualizar análisis existentes con dias_entrega basado en duracion_estimada_horas
UPDATE analisis
SET dias_entrega = CEILING(duracion_estimada_horas / 8.0)::INTEGER
WHERE dias_entrega IS NULL AND duracion_estimada_horas IS NOT NULL;

-- Establecer dias_entrega mínimo de 1
UPDATE analisis
SET dias_entrega = 1
WHERE dias_entrega IS NULL OR dias_entrega < 1;
