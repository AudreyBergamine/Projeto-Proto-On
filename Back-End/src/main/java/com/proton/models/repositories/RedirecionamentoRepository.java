package com.proton.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.redirecionamento.Redirecionamento;

public interface RedirecionamentoRepository extends JpaRepository<Redirecionamento, Integer> {

    List<Redirecionamento> findByFuncionario(Funcionario funcionario);
    
}
