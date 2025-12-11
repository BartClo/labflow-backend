-- Crear tablas de inventario: Equipos, Reactivos e Insumos

-- Tabla de Equipos
CREATE TABLE equipo (
    equipo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(200) NOT NULL,
    codigo VARCHAR(100) UNIQUE,
    marca VARCHAR(100),
    modelo VARCHAR(100),
    numero_serie VARCHAR(150),
    ubicacion VARCHAR(200),
    fecha_calibracion DATE,
    proxima_calibracion DATE,
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para Equipos
CREATE INDEX idx_equipo_codigo ON equipo(codigo);
CREATE INDEX idx_equipo_activo ON equipo(activo);
CREATE INDEX idx_equipo_proxima_calibracion ON equipo(proxima_calibracion);

-- Tabla de Reactivos
CREATE TABLE reactivo (
    reactivo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(200) NOT NULL,
    codigo VARCHAR(100) UNIQUE,
    numero_cas VARCHAR(50),
    marca VARCHAR(100),
    lote VARCHAR(100),
    concentracion VARCHAR(100),
    fecha_vencimiento DATE,
    fecha_apertura DATE,
    stock_actual DECIMAL(10, 3),
    stock_minimo DECIMAL(10, 3),
    unidad_medida VARCHAR(50),
    ubicacion_almacenamiento VARCHAR(200),
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para Reactivos
CREATE INDEX idx_reactivo_codigo ON reactivo(codigo);
CREATE INDEX idx_reactivo_activo ON reactivo(activo);
CREATE INDEX idx_reactivo_fecha_vencimiento ON reactivo(fecha_vencimiento);
CREATE INDEX idx_reactivo_stock ON reactivo(stock_actual, stock_minimo);

-- Tabla de Insumos
CREATE TABLE insumo (
    insumo_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nombre VARCHAR(200) NOT NULL,
    codigo VARCHAR(100) UNIQUE,
    categoria VARCHAR(100),
    marca VARCHAR(100),
    descripcion TEXT,
    stock_actual DECIMAL(10, 3),
    stock_minimo DECIMAL(10, 3),
    unidad_medida VARCHAR(50),
    ubicacion_almacenamiento VARCHAR(200),
    observaciones TEXT,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices para Insumos
CREATE INDEX idx_insumo_codigo ON insumo(codigo);
CREATE INDEX idx_insumo_activo ON insumo(activo);
CREATE INDEX idx_insumo_categoria ON insumo(categoria);
CREATE INDEX idx_insumo_stock ON insumo(stock_actual, stock_minimo);

-- Comentarios para documentación
COMMENT ON TABLE equipo IS 'Equipos de laboratorio con control de calibración';
COMMENT ON TABLE reactivo IS 'Reactivos químicos con control de vencimiento y stock';
COMMENT ON TABLE insumo IS 'Insumos generales de laboratorio con control de stock';
