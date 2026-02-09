package com.labflow.model;

/**
 * Enum representing the different workflow stages in an Orden de Trabajo.
 * Stages follow a sequential order from reception to final validation.
 */
public enum TipoEtapaWorkflow {
    REGISTRO_RECEPCION("Registro de Recepción", 1),
    PREPARACION_MUESTRA("Preparación de Muestra", 2),
    CONTROL_CALIDAD("Control de Calidad", 3),
    VALIDACION_RESULTADOS("Validación de Resultados", 4);

    private final String displayName;
    private final int orden;

    TipoEtapaWorkflow(String displayName, int orden) {
        this.displayName = displayName;
        this.orden = orden;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getOrden() {
        return orden;
    }

    /**
     * Returns the next stage in the workflow sequence, or null if this is the last stage.
     */
    public TipoEtapaWorkflow getSiguienteEtapa() {
        switch (this) {
            case REGISTRO_RECEPCION:
                return PREPARACION_MUESTRA;
            case PREPARACION_MUESTRA:
                return CONTROL_CALIDAD;
            case CONTROL_CALIDAD:
                return VALIDACION_RESULTADOS;
            case VALIDACION_RESULTADOS:
                return null;
            default:
                return null;
        }
    }

    /**
     * Returns the previous stage in the workflow sequence, or null if this is the first stage.
     */
    public TipoEtapaWorkflow getEtapaAnterior() {
        switch (this) {
            case REGISTRO_RECEPCION:
                return null;
            case PREPARACION_MUESTRA:
                return REGISTRO_RECEPCION;
            case CONTROL_CALIDAD:
                return PREPARACION_MUESTRA;
            case VALIDACION_RESULTADOS:
                return CONTROL_CALIDAD;
            default:
                return null;
        }
    }

    /**
     * Checks if this stage is the last stage in the workflow.
     */
    public boolean isUltimaEtapa() {
        return this == VALIDACION_RESULTADOS;
    }

    /**
     * Checks if this stage is the first stage in the workflow.
     */
    public boolean isPrimeraEtapa() {
        return this == REGISTRO_RECEPCION;
    }
}
