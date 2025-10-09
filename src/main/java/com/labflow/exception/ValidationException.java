package com.labflow.exception;

/**
 * Excepción lanzada cuando hay errores de validación de datos
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}