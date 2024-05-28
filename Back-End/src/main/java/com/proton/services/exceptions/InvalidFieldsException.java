package com.proton.services.exceptions;

public class InvalidFieldsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public InvalidFieldsException(String message){
		super(message);
	}
    public InvalidFieldsException(String fieldName, String message) {
        super("Campo inválido '" + fieldName + "': " + message);
    }
    
}
