package com.proton.models.redirecionamento;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.protocolo.Protocolo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RedirecionamentoPK{
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "protocolo_id_fk")
    private Protocolo protocolo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "funcionario_id_fk")
    private Funcionario funcionario;

    // private Funcionario funcionario;
}
