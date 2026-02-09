-- Create workflow stages table for tracking OT execution phases
CREATE TABLE orden_trabajo_etapas (
    id_etapa UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    orden_trabajo_id UUID NOT NULL,
    tipo_etapa VARCHAR(50) NOT NULL CHECK (
        tipo_etapa IN (
            'REGISTRO_RECEPCION',
            'PREPARACION_MUESTRA',
            'CONTROL_CALIDAD',
            'VALIDACION_RESULTADOS'
        )
    ),
    orden_secuencia INTEGER NOT NULL CHECK (orden_secuencia BETWEEN 1 AND 4),
    estado_etapa VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE' CHECK (
        estado_etapa IN ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADO')
    ),
    fecha_inicio TIMESTAMP,
    fecha_completado TIMESTAMP,
    tecnico_asignado_id UUID,
    notas TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign key constraints
    CONSTRAINT fk_orden_trabajo_etapa_orden
        FOREIGN KEY (orden_trabajo_id) 
        REFERENCES ordenes_trabajo(id_orden_trabajo) 
        ON DELETE CASCADE,
    CONSTRAINT fk_orden_trabajo_etapa_tecnico
        FOREIGN KEY (tecnico_asignado_id) 
        REFERENCES usuarios(id) 
        ON DELETE SET NULL,
    
    -- Ensure unique stage types per OT
    CONSTRAINT uk_orden_trabajo_tipo_etapa 
        UNIQUE (orden_trabajo_id, tipo_etapa),
    
    -- Ensure unique sequence per OT
    CONSTRAINT uk_orden_trabajo_secuencia 
        UNIQUE (orden_trabajo_id, orden_secuencia),
    
    -- Validate fecha_completado is after fecha_inicio
    CONSTRAINT chk_fechas_etapa 
        CHECK (fecha_completado IS NULL OR fecha_inicio IS NULL OR fecha_completado >= fecha_inicio)
);

-- Create indexes for better query performance
CREATE INDEX idx_orden_trabajo_etapas_orden_trabajo 
    ON orden_trabajo_etapas(orden_trabajo_id);

CREATE INDEX idx_orden_trabajo_etapas_estado 
    ON orden_trabajo_etapas(estado_etapa);

CREATE INDEX idx_orden_trabajo_etapas_tecnico 
    ON orden_trabajo_etapas(tecnico_asignado_id);

-- Trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_orden_trabajo_etapa_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_orden_trabajo_etapa_timestamp
    BEFORE UPDATE ON orden_trabajo_etapas
    FOR EACH ROW
    EXECUTE FUNCTION update_orden_trabajo_etapa_timestamp();
