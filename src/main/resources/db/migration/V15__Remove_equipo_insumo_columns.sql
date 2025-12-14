-- Remover columnas de las tablas Equipo e Insumo

-- Eliminar índices de Equipo antes de eliminar las columnas
DROP INDEX IF EXISTS idx_equipo_codigo;

-- Eliminar columnas de la tabla Equipo
ALTER TABLE equipo DROP COLUMN IF EXISTS codigo;
ALTER TABLE equipo DROP COLUMN IF EXISTS marca;

-- Eliminar índices de Insumo antes de eliminar las columnas
DROP INDEX IF EXISTS idx_insumo_codigo;
DROP INDEX IF EXISTS idx_insumo_categoria;

-- Eliminar columnas de la tabla Insumo
ALTER TABLE insumo DROP COLUMN IF EXISTS codigo;
ALTER TABLE insumo DROP COLUMN IF EXISTS categoria;
