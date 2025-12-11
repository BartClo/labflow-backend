-- Agregar campos de paquete comercial a la tabla plantillas

ALTER TABLE plantillas
ADD COLUMN es_paquete_comercial BOOLEAN DEFAULT FALSE,
ADD COLUMN precio_paquete DECIMAL(15, 2),
ADD COLUMN codigo_paquete VARCHAR(100);

-- Crear índice para búsqueda por código de paquete
CREATE INDEX idx_plantillas_codigo_paquete ON plantillas(codigo_paquete);

-- Comentario para documentación
COMMENT ON COLUMN plantillas.es_paquete_comercial IS 'Indica si la plantilla representa un paquete comercial';
COMMENT ON COLUMN plantillas.precio_paquete IS 'Precio del paquete comercial cuando es aplicable';
COMMENT ON COLUMN plantillas.codigo_paquete IS 'Código único del paquete comercial para facturación';
