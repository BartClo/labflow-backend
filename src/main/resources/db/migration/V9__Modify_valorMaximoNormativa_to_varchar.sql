-- Modificar columna valor_maximo_normativa de DECIMAL a VARCHAR
-- Permite almacenar valores de texto como "Ausencia", "<LDM", etc.

ALTER TABLE parametro
ALTER COLUMN valor_maximo_normativa TYPE VARCHAR(255);
