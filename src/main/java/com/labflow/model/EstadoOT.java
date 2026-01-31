package com.labflow.model;

/**
 * Estados posibles de una Orden de Trabajo
 */
public enum EstadoOT {
    /**
     * Orden creada y lista para asignar tareas
     */
    ABIERTA,
    
    /**
     * Orden en proceso de ejecución
     */
    EN_PROCESO,
    
    /**
     * Orden completada con todos los análisis finalizados
     */
    FINALIZADA,
    
    /**
     * Orden cancelada
     */
    CANCELADA
}
