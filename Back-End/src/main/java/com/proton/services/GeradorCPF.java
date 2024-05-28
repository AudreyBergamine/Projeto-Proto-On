package com.proton.services;

import java.util.Random;

public class GeradorCPF {

    public static String generateRandomCPF() {
        Random random = new Random();
        int cpfSemDV = random.nextInt(900_000_000) + 100_000_000; // Gera um número aleatório de 9 dígitos

        // Calcula o primeiro dígito verificador
        int dv1 = calculateDigit(cpfSemDV);
        cpfSemDV = cpfSemDV * 10 + dv1; // Adiciona o primeiro dígito verificador ao número

        // Calcula o segundo dígito verificador
        int dv2 = calculateDigit(cpfSemDV);
        cpfSemDV = cpfSemDV * 10 + dv2; // Adiciona o segundo dígito verificador ao número

        return String.format("%011d", cpfSemDV); // Formata o CPF com zeros à esquerda
    }

    public static int calculateDigit(int cpfSemDV) {
        int soma = 0;
        int peso = 2;
        for (int i = 8; i >= 0; i--) {
            int digito = (cpfSemDV / (int) Math.pow(10, i)) % 10;
            soma += digito * peso;
            peso++;
        }
        int resto = soma % 11;
        int dv = 11 - resto;
        if (dv >= 10) {
            dv = 0;
        }
        return dv;  
    }
}

