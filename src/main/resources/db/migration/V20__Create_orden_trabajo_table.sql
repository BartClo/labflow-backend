-- V20: Crear tabla de órdenes de trabajo y relación con muestra_analisis
-- Esta migración implementa el sistema de agrupación de tareas en órdenes de trabajo

-- Eliminar tabla si existe (para desarrollo)
DROP TABLE IF EXISTS ordenes_trabajo CASCADE;

-- Crear tabla ordenes_trabajo
CREATE TABLE ordenes_trabajo (
    id_orden_trabajo UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    estado VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' 
        CHECK (estado IN ('PENDIENTE', 'EN_PROCESO', 'COMPLETADA', 'CANCELADA')),
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_finalizacion TIMESTAMP,
    tecnico_asignado_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key a tabla usuarios
    CONSTRAINT fk_orden_trabajo_tecnico 
        FOREIGN KEY (tecnico_asignado_id) 
        REFERENCES usuarios(id) 
        ON DELETE RESTRICT
);

-- Agregar columna orden_trabajo_id a muestra_analisis
ALTER TABLE muestra_analisis 
ADD COLUMN IF NOT EXISTS orden_trabajo_id UUID;

-- Agregar foreign key constraint
ALTER TABLE muestra_analisis
DROP CONSTRAINT IF EXISTS fk_muestra_analisis_orden_trabajo;

ALTER TABLE muestra_analisis
ADD CONSTRAINT fk_muestra_analisis_orden_trabajo 
    FOREIGN KEY (orden_trabajo_id) 
    REFERENCES ordenes_trabajo(id_orden_trabajo) 
    ON DELETE SET NULL;

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_muestra_analisis_orden_trabajo 
    ON muestra_analisis(orden_trabajo_id);

CREATE INDEX IF NOT EXISTS idx_orden_trabajo_tecnico 
    ON ordenes_trabajo(tecnico_asignado_id);

CREATE INDEX IF NOT EXISTS idx_orden_trabajo_estado 
    ON ordenes_trabajo(estado);

CREATE INDEX IF NOT EXISTS idx_orden_trabajo_fecha_creacion 
    ON ordenes_trabajo(fecha_creacion DESC);

-- Índice compuesto para búsquedas frecuentes (tareas pendientes sin OT)
CREATE INDEX IF NOT EXISTS idx_muestra_analisis_estado_orden 
    ON muestra_analisis(estado_analisis, orden_trabajo_id);

-- Comentarios para documentación
COMMENT ON TABLE ordenes_trabajo IS 'Órdenes de trabajo que agrupan tareas de análisis para ser procesadas por un técnico';

COMMENT ON COLUMN ordenes_trabajo.id_orden_trabajo IS 'Identificador único de la orden de trabajo';
COMMENT ON COLUMN ordenes_trabajo.estado IS 'Estado actual de la orden: PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA';
COMMENT ON COLUMN ordenes_trabajo.fecha_creacion IS 'Fecha y hora de creación de la orden de trabajo';
COMMENT ON COLUMN ordenes_trabajo.fecha_finalizacion IS 'Fecha y hora de finalización (completada o cancelada)';
COMMENT ON COLUMN ordenes_trabajo.tecnico_asignado_id IS 'Técnico responsable de procesar esta orden de trabajo';

COMMENT ON COLUMN muestra_analisis.orden_trabajo_id IS 'Orden de trabajo a la que pertenece esta tarea (nullable - las tareas pueden crearse sin OT)';
