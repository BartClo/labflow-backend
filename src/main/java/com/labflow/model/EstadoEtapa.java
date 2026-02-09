package com.labflow.model;

/**
 * Enum representing the possible states of a workflow stage in an Orden de Trabajo.
 * Stages progress from PENDIENTE → EN_PROGRESO → COMPLETADO.
 */
public enum EstadoEtapa {
    PENDIENTE("Pendiente"),
    EN_PROGRESO("En Progreso"),
    COMPLETADO("Completado");

    private final String displayName;

    EstadoEtapa(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Checks if the stage can be initiated (transition to EN_PROGRESO).
     */
    public boolean puedeIniciar() {
        return this == PENDIENTE;
    }

    /**
     * Checks if the stage can be completed (transition to COMPLETADO).
     */
    public boolean puedeCompletar() {
        return this == EN_PROGRESO;
    }

    /**
     * Checks if the stage is already finished.
     */
    public boolean estaCompletado() {
        return this == COMPLETADO;
    }

    /**
     * Checks if the stage is currently being worked on.
     */
    public boolean estaEnProgreso() {
        return this == EN_PROGRESO;
    }
}
