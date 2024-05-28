package com.proton.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proton.models.entities.funcionario.Funcionario;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    Optional<Funcionario> findByEmail(String email);


    @Query("SELECT f.secretaria.id_secretaria FROM Funcionario f WHERE f.id = :idFuncionario")
    Long findBySecretaria(Integer idFuncionario);
    
}