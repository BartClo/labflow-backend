-- Migración V26: Agregar valor_minimo_normativa a la tabla parametro
-- Fecha: 2026-02-09
-- Descripción: Agrega la columna valor_minimo_normativa que faltaba en la tabla parametro

ALTER TABLE parametro 
ADD COLUMN valor_minimo_normativa VARCHAR(255);

-- Comentario descriptivo para documentar la columna
COMMENT ON COLUMN parametro.valor_minimo_normativa IS 'Valor mínimo permitido según normativa aplicable';
