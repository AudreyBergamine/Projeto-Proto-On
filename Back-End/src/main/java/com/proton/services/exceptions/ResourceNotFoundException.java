package com.proton.services.exceptions;


//Classe de exceção personalizada, para personalizar erros do tipo EntityNotFoundException (exceção ocorre quando não acha um valor)
public class ResourceNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ResourceNotFoundException(Object id){
		super("Resource not found. Id: " + id);
	}
	public ResourceNotFoundException(Integer id){
		super("Resource not found. Id: " + id);
	}

}
