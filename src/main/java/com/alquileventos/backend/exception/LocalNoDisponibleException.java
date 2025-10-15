package com.alquileventos.backend.exception;

public class LocalNoDisponibleException extends RuntimeException {
    
    public LocalNoDisponibleException(String mensaje) {
        super(mensaje);
    }
    
    public LocalNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
