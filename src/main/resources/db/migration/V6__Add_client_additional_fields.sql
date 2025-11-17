-- ========================================
-- Migración V6__Add_client_additional_fields.sql
-- ========================================
-- Agrega campos adicionales a la tabla clientes para datos completos del cliente
-- Fecha: 2025-11-16
-- Autor: LabFlow Team

-- Agregar nuevas columnas a la tabla clientes
ALTER TABLE clientes
    ADD COLUMN nombre VARCHAR(100),
    ADD COLUMN empresa VARCHAR(200),
    ADD COLUMN email VARCHAR(100),
    ADD COLUMN telefono VARCHAR(20),
    ADD COLUMN direccion VARCHAR(255),
    ADD COLUMN persona_contacto VARCHAR(100),
    ADD COLUMN tipo_cliente VARCHAR(50) DEFAULT 'Empresa',
    ADD COLUMN activo BOOLEAN DEFAULT TRUE;

-- Migrar datos existentes: mover nombre_cliente a empresa si existe
UPDATE clientes 
SET empresa = nombre_cliente 
WHERE empresa IS NULL AND nombre_cliente IS NOT NULL;

-- Crear índices para mejorar performance
CREATE INDEX idx_clientes_email ON clientes(email);
CREATE INDEX idx_clientes_tipo ON clientes(tipo_cliente);
CREATE INDEX idx_clientes_activo ON clientes(activo);

-- Agregar comentarios a las nuevas columnas
COMMENT ON COLUMN clientes.nombre IS 'Nombre completo de la persona de contacto principal';
COMMENT ON COLUMN clientes.empresa IS 'Nombre de la empresa o razón social';
COMMENT ON COLUMN clientes.email IS 'Correo electrónico de contacto';
COMMENT ON COLUMN clientes.telefono IS 'Número de teléfono de contacto';
COMMENT ON COLUMN clientes.direccion IS 'Dirección física del cliente';
COMMENT ON COLUMN clientes.persona_contacto IS 'Nombre de la persona de contacto alternativa';
COMMENT ON COLUMN clientes.tipo_cliente IS 'Tipo de cliente (Empresa, Persona, etc.)';
COMMENT ON COLUMN clientes.activo IS 'Indica si el cliente está activo en el sistema';
