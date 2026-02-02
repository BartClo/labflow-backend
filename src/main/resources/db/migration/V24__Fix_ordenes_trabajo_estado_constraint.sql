-- V24: Actualizar constraint de estado en ordenes_trabajo
-- El enum Java usa ABIERTA, FINALIZADA en lugar de PENDIENTE, COMPLETADA

-- Eliminar constraint actual
ALTER TABLE ordenes_trabajo DROP CONSTRAINT IF EXISTS ordenes_trabajo_estado_check;

-- Crear nuevo constraint con los valores correctos del enum Java
ALTER TABLE ordenes_trabajo ADD CONSTRAINT ordenes_trabajo_estado_check 
    CHECK (estado IN ('ABIERTA', 'EN_PROCESO', 'FINALIZADA', 'CANCELADA'));

-- Actualizar registros existentes si los hay (PENDIENTE -> ABIERTA, COMPLETADA -> FINALIZADA)
UPDATE ordenes_trabajo SET estado = 'ABIERTA' WHERE estado = 'PENDIENTE';
UPDATE ordenes_trabajo SET estado = 'FINALIZADA' WHERE estado = 'COMPLETADA';

-- Actualizar comentario
COMMENT ON COLUMN ordenes_trabajo.estado IS 'Estado actual de la orden: ABIERTA, EN_PROCESO, FINALIZADA, CANCELADA';
