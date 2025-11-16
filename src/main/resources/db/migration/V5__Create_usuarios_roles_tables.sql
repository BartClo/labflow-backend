-- =====================================================
-- Migración V5: Tablas de Usuarios y Roles
-- =====================================================

-- Tabla de roles
CREATE TABLE roles (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    permisos TEXT[],
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de usuarios
CREATE TABLE usuarios (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion TEXT,
    fecha_nacimiento DATE,
    rol_id UUID NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    ultima_conexion TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE RESTRICT
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_usuarios_email ON usuarios(email);
CREATE INDEX idx_usuarios_username ON usuarios(username);
CREATE INDEX idx_usuarios_rol_id ON usuarios(rol_id);
CREATE INDEX idx_usuarios_activo ON usuarios(activo);
CREATE INDEX idx_roles_nombre ON roles(nombre);
CREATE INDEX idx_roles_activo ON roles(activo);

-- Función para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Triggers para updated_at
CREATE TRIGGER update_roles_updated_at
    BEFORE UPDATE ON roles
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_usuarios_updated_at
    BEFORE UPDATE ON usuarios
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- =====================================================
-- Datos iniciales: Roles
-- =====================================================

INSERT INTO roles (nombre, descripcion, permisos, activo) VALUES
(
    'ADMINISTRADOR',
    'Rol con acceso completo al sistema. Puede gestionar usuarios, configuraciones y todos los módulos.',
    ARRAY['MANAGE_USERS', 'MANAGE_ROLES', 'MANAGE_CLIENTES', 'MANAGE_ANALISIS', 'MANAGE_PLANTILLAS', 'MANAGE_MUESTRAS', 'VIEW_REPORTS', 'MANAGE_SYSTEM'],
    TRUE
),
(
    'TRABAJADOR',
    'Rol con acceso operativo al sistema. Puede gestionar clientes, análisis, plantillas y muestras.',
    ARRAY['VIEW_USERS', 'MANAGE_CLIENTES', 'MANAGE_ANALISIS', 'MANAGE_PLANTILLAS', 'MANAGE_MUESTRAS', 'VIEW_REPORTS'],
    TRUE
);

-- =====================================================
-- Constraints adicionales
-- =====================================================

-- Constraint para validar formato de email
ALTER TABLE usuarios ADD CONSTRAINT check_email_format 
    CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');

-- Constraint para validar longitud mínima de username
ALTER TABLE usuarios ADD CONSTRAINT check_username_length 
    CHECK (length(username) >= 3);

-- Constraint para validar que el nombre no esté vacío
ALTER TABLE usuarios ADD CONSTRAINT check_nombre_not_empty 
    CHECK (trim(nombre) <> '');

-- Constraint para validar que el apellido no esté vacío
ALTER TABLE usuarios ADD CONSTRAINT check_apellido_not_empty 
    CHECK (trim(apellido) <> '');

-- Comentarios en las tablas
COMMENT ON TABLE roles IS 'Tabla que almacena los diferentes roles del sistema';
COMMENT ON TABLE usuarios IS 'Tabla que almacena los usuarios del sistema con sus datos personales';
COMMENT ON COLUMN usuarios.password IS 'Contraseña encriptada del usuario';
COMMENT ON COLUMN usuarios.ultima_conexion IS 'Timestamp de la última vez que el usuario inició sesión';
COMMENT ON COLUMN roles.permisos IS 'Array de permisos asignados al rol';
