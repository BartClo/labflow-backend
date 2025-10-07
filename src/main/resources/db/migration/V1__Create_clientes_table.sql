-- ========================================
-- Migración V1__Create_clientes_table.sql
-- ========================================
-- Crea la tabla de clientes con UUID como primary key
-- Fecha: 2025-10-07
-- Autor: LabFlow Team

-- Habilitar extensión UUID si no está habilitada
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Crear tabla clientes
CREATE TABLE clientes (
    id_cliente UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    nombre_cliente VARCHAR(255) NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índice para mejorar performance en búsquedas por nombre
CREATE INDEX idx_clientes_nombre ON clientes(nombre_cliente);

-- Agregar comentarios a la tabla y columnas
COMMENT ON TABLE clientes IS 'Tabla de clientes del sistema LabFlow';
COMMENT ON COLUMN clientes.id_cliente IS 'Identificador único del cliente (UUID)';
COMMENT ON COLUMN clientes.nombre_cliente IS 'Nombre completo del cliente';
COMMENT ON COLUMN clientes.fecha_creacion IS 'Fecha y hora de creación del registro';
COMMENT ON COLUMN clientes.fecha_actualizacion IS 'Fecha y hora de última actualización';

-- Crear función para actualizar automáticamente fecha_actualizacion
CREATE OR REPLACE FUNCTION update_fecha_actualizacion_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.fecha_actualizacion = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Crear trigger para actualizar automáticamente fecha_actualizacion
CREATE TRIGGER update_clientes_fecha_actualizacion 
    BEFORE UPDATE ON clientes 
    FOR EACH ROW 
    EXECUTE FUNCTION update_fecha_actualizacion_column();