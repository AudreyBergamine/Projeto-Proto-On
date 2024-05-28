package com.proton.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proton.models.entities.Log;

public interface LogRepository extends JpaRepository<Log, Long> {
    
}

