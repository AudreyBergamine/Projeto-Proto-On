package com.proton.services.exceptions;

public class RedirecionamentoDuplicadoException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public RedirecionamentoDuplicadoException(String message) {
        super(message);
    }
}