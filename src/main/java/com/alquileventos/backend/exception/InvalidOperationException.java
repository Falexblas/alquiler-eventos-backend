package com.alquileventos.backend.exception;

public class InvalidOperationException extends RuntimeException {
    
    public InvalidOperationException(String mensaje) {
        super(mensaje);
    }
    
    public InvalidOperationException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
