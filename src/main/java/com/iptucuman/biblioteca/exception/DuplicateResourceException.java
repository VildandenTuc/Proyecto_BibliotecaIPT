package com.iptucuman.biblioteca.exception;

/**
 * Excepción personalizada para recursos duplicados.
 * Se lanza cuando se intenta crear o actualizar un recurso con datos únicos ya existentes.
 * Debe ser capturada por GlobalExceptionHandler para retornar HTTP 409 CONFLICT.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
