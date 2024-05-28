package com.proton.models.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proton.models.entities.recuper_senha.RecuperarSenha;

public interface RecuperarSenhaRepository extends JpaRepository<RecuperarSenha, String> {
  Optional<RecuperarSenha> findByEmail(String email);

  Optional<RecuperarSenha> findByEmailAndCodigo(String email, String codigo);
}
