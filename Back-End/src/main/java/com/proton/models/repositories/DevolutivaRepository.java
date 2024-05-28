package com.proton.models.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.proton.models.entities.protocolo.Devolutiva;

@Repository
public interface DevolutivaRepository extends JpaRepository<Devolutiva, Integer> {

    @Query("SELECT d FROM Devolutiva d WHERE d.id_protocolo.id = :idProtocolo")
    List<Devolutiva> findByIdProtocolo(Integer idProtocolo);
    
}


