package com.proton.controller.resources.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.proton.services.exceptions.ConstraintException;
import com.proton.services.exceptions.InvalidFieldsException;
import com.proton.services.exceptions.RedirecionamentoDuplicadoException;
import com.proton.services.exceptions.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

//ESTA CLASSE SERVE PARA TRATARMOS TODAS AS EXCEÇÕES COM MENSAGENS PERSONALIZADAS (COM AS MENSAGENS QUE QUISERMOS)

//Intercepta as exceções para esta classe efetuar o tratamento
@ControllerAdvice
public class ResourceExceptionHandler {

    //MÉTODO PARA TRATAR AS EXCEÇÕES DE RECURSOS NÃO ENCONTRADOS
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StardandError> resourceNotFound(ResourceNotFoundException e, HttpServletRequest request){
        //Mensagem de erro genérica do erro encontrado.
        String error = "Resource not found";

        //Código do erro encontrado NOT_FOUND = 404
        HttpStatus status = HttpStatus.NOT_FOUND;
        
        //Instancia uma mensagem de erro personalizada, com os parâmetros abaixo
        StardandError err = new StardandError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    //MÉTODO PARA TRATAR EXCEÇÕES DE INTEGRIDADE DE DADOS (EX: ERRO DE UNIQUE NO BANCO DE DADOS)
    @ExceptionHandler(ConstraintException.class)
    public ResponseEntity<StardandError> constraint(ConstraintException e, HttpServletRequest request){
        //Erro genérico do problema
        String error = "Integrity Constraint Error";

        //Código do erro encontrado BAD_REQUEST = 400
        HttpStatus status = HttpStatus.BAD_REQUEST;

        //Instancia uma mensagem de erro personalizada, com os parâmetros abaixo
        StardandError err = new StardandError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(InvalidFieldsException.class)
    public ResponseEntity<StardandError> constraint(InvalidFieldsException e, HttpServletRequest request){
        //Erro genérico do problema
        String error = "Invalid Fields Error";

        //Código do erro encontrado BAD_REQUEST = 400
        HttpStatus status = HttpStatus.BAD_REQUEST;

        //Instancia uma mensagem de erro personalizada, com os parâmetros abaixo
        StardandError err = new StardandError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }

    @ExceptionHandler(RedirecionamentoDuplicadoException.class)
    public ResponseEntity<StardandError> constraint(RedirecionamentoDuplicadoException e, HttpServletRequest request){
        //Erro genérico do problema
        String error = "Invalid Redirecionamento Error";

        //Código do erro encontrado BAD_REQUEST = 400
        HttpStatus status = HttpStatus.BAD_REQUEST;

        //Instancia uma mensagem de erro personalizada, com os parâmetros abaixo
        StardandError err = new StardandError(Instant.now(), status.value(), error, e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(err);
    }
    
    
}
