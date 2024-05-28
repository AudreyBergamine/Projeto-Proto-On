package com.proton.services.exceptions;

//Classe de exceção personalizada, para personalizar erros do tipo DataIntegrityViolationException
public class ConstraintException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public ConstraintException(String message){
		super(message);
	}

}
