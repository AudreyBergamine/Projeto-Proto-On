// INTERFACE DO ENDEREÇO QUE EXTENDE O JPA REPOSITORY PARA ACESSAR OS METODOS DO JPA REPOSITORY
// ESSA INTERFACE É USADA PARA ACESSAR OS DADOS DO BANCO DE DADOS

package com.proton.models.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proton.models.entities.municipe.Municipe;
import com.proton.models.entities.protocolo.Protocolo;

public interface ProtocoloRepository extends JpaRepository<Protocolo, Integer> {
    List<Protocolo> findAllByMunicipe(Municipe municipe);

    @Query("SELECT p FROM Protocolo p WHERE p.numero_protocolo = :numeroProtocolo")
    Optional<Protocolo> findByNumeroProtocolo(@Param("numeroProtocolo") String numeroProtocolo);

    @Query("SELECT p FROM Protocolo p WHERE p.municipe.id = :id_municipe")
    List<Protocolo> findByMunicipe(Integer id_municipe);
}
