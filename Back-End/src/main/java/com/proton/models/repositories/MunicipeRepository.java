// INTERFACE DO MUNICIPE QUE EXTENDE O JPA REPOSITORY PARA ACESSAR OS METODOS DO JPA REPOSITORY
// ESSA INTERFACE É USADA PARA ACESSAR OS DADOS DO BANCO DE DADOS

package com.proton.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proton.models.entities.municipe.Municipe;

@Repository
public interface MunicipeRepository extends JpaRepository<Municipe, Integer> {
   Optional<Municipe> findByEmail(String email);
   Optional<Municipe> findByNome(String Nome);
}
