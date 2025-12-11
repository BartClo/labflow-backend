-- Crear tabla de relación entre Análisis y Recursos (Equipos, Reactivos, Insumos)

CREATE TABLE analisis_recurso (
    analisis_recurso_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    id_analisis UUID NOT NULL,
    equipo_id UUID,
    reactivo_id UUID,
    insumo_id UUID,
    cantidad_requerida DECIMAL(10, 3),
    observaciones TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign keys
    CONSTRAINT fk_analisis_recurso_analisis FOREIGN KEY (id_analisis) 
        REFERENCES analisis(id_analisis) ON DELETE CASCADE,
    CONSTRAINT fk_analisis_recurso_equipo FOREIGN KEY (equipo_id) 
        REFERENCES equipo(equipo_id) ON DELETE SET NULL,
    CONSTRAINT fk_analisis_recurso_reactivo FOREIGN KEY (reactivo_id) 
        REFERENCES reactivo(reactivo_id) ON DELETE SET NULL,
    CONSTRAINT fk_analisis_recurso_insumo FOREIGN KEY (insumo_id) 
        REFERENCES insumo(insumo_id) ON DELETE SET NULL,
    
    -- Validación: al menos uno de los recursos debe estar presente
    CONSTRAINT chk_al_menos_un_recurso CHECK (
        equipo_id IS NOT NULL OR 
        reactivo_id IS NOT NULL OR 
        insumo_id IS NOT NULL
    )
);

-- Índices para mejorar el rendimiento de las consultas
CREATE INDEX idx_analisis_recurso_analisis ON analisis_recurso(id_analisis);
CREATE INDEX idx_analisis_recurso_equipo ON analisis_recurso(equipo_id);
CREATE INDEX idx_analisis_recurso_reactivo ON analisis_recurso(reactivo_id);
CREATE INDEX idx_analisis_recurso_insumo ON analisis_recurso(insumo_id);

-- Comentario para documentación
COMMENT ON TABLE analisis_recurso IS 'Relaciona análisis con los recursos (equipos, reactivos, insumos) necesarios';
COMMENT ON CONSTRAINT chk_al_menos_un_recurso ON analisis_recurso IS 'Garantiza que cada registro tenga al menos un recurso asociado';
