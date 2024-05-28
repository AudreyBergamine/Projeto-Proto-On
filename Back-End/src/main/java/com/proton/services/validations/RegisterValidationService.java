package com.proton.services.validations;

import java.util.InputMismatchException;

import org.springframework.stereotype.Service;

import com.proton.controller.resources.auth.requests.RegisterRequestMunicipe;
import com.proton.services.exceptions.InvalidFieldsException;

@Service
public class RegisterValidationService {



    //Função que valida os campos do cadastro de Municipes
    public void validateMunicipeFields(RegisterRequestMunicipe registerRequestMunicipe){

        if(registerRequestMunicipe.getEndereco().getNum_cep().length()!=9){
            throw new InvalidFieldsException("Cep", "O cep deve conter 9 caracteries");
        }
        else if(registerRequestMunicipe.getNome().length()<4){
            throw new InvalidFieldsException("Nome", "O nome deve conter ao menos 4 caracteries ou mais"); 
        }
        else if(registerRequestMunicipe.getSenha().length()<6){
            throw new InvalidFieldsException("Senha", "A senha deve conter ao menos 6 caracteries ou mais"); 
        }
        else if(registerRequestMunicipe.getNum_CPF().length() != 14){
            throw new InvalidFieldsException("CPF", "O CPF deve conter 14 caracteries"); 
        }
    }



    

    //Função que verifica se o cpf é válido
    public void validateCPF(String CPF) {
        // Remova os pontos e hífen do CPF
        CPF = CPF.replaceAll("[^0-9]", "");
    
        // Verifica se o CPF tem 11 dígitos após a remoção de caracteres não numéricos
        if (CPF.length() != 11) {
            throw new InvalidFieldsException("CPF", "O CPF fornecido deve conter exatamente 11 dígitos, contando apenas números.");
        }
    
        // Verifica se todos os dígitos são iguais
        if (CPF.matches("(\\d)\\1{10}")) {
            throw new InvalidFieldsException("CPF", "O CPF fornecido não pode consistir em uma sequência de números iguais.");
        }
    
        try {
            // Calcula o primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += (10 - i) * Integer.parseInt(String.valueOf(CPF.charAt(i)));
            }
            int digito1 = 11 - (soma % 11);
            if (digito1 > 9) {
                digito1 = 0;
            }
    
            // Calcula o segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += (11 - i) * Integer.parseInt(String.valueOf(CPF.charAt(i)));
            }
            int digito2 = 11 - (soma % 11);
            if (digito2 > 9) {
                digito2 = 0;
            }
    
            // Verifica se os dígitos verificadores calculados conferem com os dígitos informados
            if (digito1 != Integer.parseInt(String.valueOf(CPF.charAt(9))) ||
                digito2 != Integer.parseInt(String.valueOf(CPF.charAt(10)))) {
                throw new InvalidFieldsException("CPF", "O CPF fornecido é inválido. Verifique se os dígitos de verificação estão corretos.");
            }
        } catch (NumberFormatException | InputMismatchException e) {
            throw new InvalidFieldsException("CPF", "O CPF fornecido é inválido. Verifique se está no formato correto e tente novamente.");
        }
    }


   
    
    
}
