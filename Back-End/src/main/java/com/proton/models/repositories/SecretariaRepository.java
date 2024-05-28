package com.proton.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proton.models.entities.secretaria.Secretaria;

public interface SecretariaRepository extends JpaRepository<Secretaria, Long> {
    
}
