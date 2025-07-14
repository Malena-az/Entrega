package com.calyma.backend.exception;

public class ProductoDuplicadoException extends RuntimeException {
    
    public ProductoDuplicadoException(String message) {
        super(message);
    }
    
    public ProductoDuplicadoException(String message, Throwable cause) {
        super(message, cause);
    }
} 