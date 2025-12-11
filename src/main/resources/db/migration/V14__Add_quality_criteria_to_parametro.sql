-- Migración V14: Agregar criterios de control de calidad a la tabla parametro
-- Fecha: 2025-12-11
-- Descripción: Agrega columnas para almacenar criterios de precisión, exactitud, spike y blanco
--              provenientes del archivo Excel "Controles LABCAUSS.xlsx"

ALTER TABLE parametro 
ADD COLUMN criterio_precision TEXT,
ADD COLUMN criterio_exactitud TEXT,
ADD COLUMN criterio_spike TEXT,
ADD COLUMN criterio_blanco TEXT;

-- Comentarios descriptivos para documentar las columnas
COMMENT ON COLUMN parametro.criterio_precision IS 'Criterio de precisión (duplicados) - Columna C de Controles LABCAUSS.xlsx. Ejemplo: "0.9" o fórmulas como "lg D1 - lg D2"';
COMMENT ON COLUMN parametro.criterio_exactitud IS 'Criterio de exactitud (estándar) - Columna D de Controles LABCAUSS.xlsx. Ejemplo: "100±10%"';
COMMENT ON COLUMN parametro.criterio_spike IS 'Criterio de spike/fortificada - Columna E de Controles LABCAUSS.xlsx. Ejemplo: "100±20%"';
COMMENT ON COLUMN parametro.criterio_blanco IS 'Criterio de blanco - Columna F de Controles LABCAUSS.xlsx. Ejemplo: "<LDM" o "No debe mostrar crecimiento"';
