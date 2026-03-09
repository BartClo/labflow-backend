-- V28: Agregar prioridad a órdenes de trabajo y crear tabla imagenes 1:1 con muestras

-- =====================================================
-- PRIORIDAD EN ORDENES_TRABAJO
-- =====================================================

ALTER TABLE ordenes_trabajo
    ADD COLUMN IF NOT EXISTS prioridad VARCHAR(20);

UPDATE ordenes_trabajo
SET prioridad = 'MEDIA'
WHERE prioridad IS NULL;

ALTER TABLE ordenes_trabajo
    ALTER COLUMN prioridad SET DEFAULT 'MEDIA';

ALTER TABLE ordenes_trabajo
    ALTER COLUMN prioridad SET NOT NULL;

ALTER TABLE ordenes_trabajo DROP CONSTRAINT IF EXISTS ordenes_trabajo_prioridad_check;

ALTER TABLE ordenes_trabajo
    ADD CONSTRAINT ordenes_trabajo_prioridad_check
    CHECK (prioridad IN ('BAJA', 'MEDIA', 'ALTA'));

CREATE INDEX IF NOT EXISTS idx_orden_trabajo_prioridad
    ON ordenes_trabajo(prioridad);

COMMENT ON COLUMN ordenes_trabajo.prioridad IS 'Prioridad de la orden: BAJA, MEDIA, ALTA';

-- =====================================================
-- TABLA IMAGENES (1:1 CON MUESTRAS)
-- =====================================================

CREATE TABLE IF NOT EXISTS imagenes (
    id_imagen UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_muestra UUID NOT NULL UNIQUE,
    contenido BYTEA NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    nombre_archivo VARCHAR(255) NOT NULL,
    tamano_bytes BIGINT NOT NULL CHECK (tamano_bytes > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_imagen_muestra
        FOREIGN KEY (id_muestra)
        REFERENCES muestras(id_muestra)
        ON DELETE CASCADE,

    CONSTRAINT imagenes_mime_type_check
        CHECK (
            lower(mime_type) = 'application/pdf'
            OR lower(mime_type) LIKE 'image/%'
        )
);

CREATE INDEX IF NOT EXISTS idx_imagenes_id_muestra
    ON imagenes(id_muestra);

COMMENT ON TABLE imagenes IS 'Archivo asociado 1:1 con una muestra (imagen o PDF)';
COMMENT ON COLUMN imagenes.contenido IS 'Contenido binario del archivo (BYTEA)';
COMMENT ON COLUMN imagenes.mime_type IS 'Tipo MIME permitido: image/* o application/pdf';
