package com.labflow.model;

/**
 * Estados posibles de una Orden de Trabajo
 */
public enum EstadoOT {
    /**
     * Orden creada pero no iniciada
     */
    PENDIENTE,
    
    /**
     * Orden en proceso de ejecución
     */
    EN_PROCESO,
    
    /**
     * Orden completada con todos los análisis finalizados
     */
    COMPLETADA,
    
    /**
     * Orden cancelada
     */
    CANCELADA
}
