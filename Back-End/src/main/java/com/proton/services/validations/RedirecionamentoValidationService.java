package com.proton.services.validations;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.proton.models.enums.StatusRedirecionamento;
import com.proton.models.redirecionamento.Redirecionamento;
import com.proton.services.exceptions.RedirecionamentoDuplicadoException;

@Service
public class RedirecionamentoValidationService {
    public void validateRedirecionamento(List<Redirecionamento> redirecionamentos, Redirecionamento novoRedirecionamento) {
        // Verificar se já existe um redirecionamento com status ANDAMENTO para o mesmo PROTOCOLO_ID
        boolean existeRedirecionamentoEmAndamento = redirecionamentos.stream()
                .anyMatch(r -> r.getProtocolo().getId_protocolo().equals(novoRedirecionamento.getProtocolo().getId_protocolo())
                        && r.getStatusRedirecionamento() == StatusRedirecionamento.ANDAMENTO);
        
        // Se já existe um redirecionamento em andamento para o mesmo PROTOCOLO_ID, lançar a exceção
        if (existeRedirecionamentoEmAndamento) {
            throw new RedirecionamentoDuplicadoException("Já existe um redirecionamento em andamento para o protocolo com número: " 
                + novoRedirecionamento.getProtocolo().getNumero_protocolo() + " e o email do funcionário responsável é: " 
                + novoRedirecionamento.getFuncionario().getEmail());
        }
    }
}