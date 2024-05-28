package com.proton.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proton.models.entities.assunto.Assunto;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {
    
}
