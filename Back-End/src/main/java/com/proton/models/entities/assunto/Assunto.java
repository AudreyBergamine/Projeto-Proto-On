package com.proton.models.entities.assunto;

import java.io.Serializable;

import com.proton.models.entities.secretaria.Secretaria;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "assunto")
public class Assunto implements Serializable {
    private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_assunto;
    private String assunto;
    private Double valor_protocolo;


    @ManyToOne
	@JoinColumn(name = "id_secretaria")
    private Secretaria Secretaria;

    public Assunto(){
    }

    public Assunto(Integer id_assunto, String assunto, Secretaria secretaria,Double valor_protocolo ) {
        this.id_assunto = id_assunto;
        this.assunto = assunto;
        Secretaria = secretaria;
        this.valor_protocolo = valor_protocolo;
    }
    public Integer getId_assunto() {
        return id_assunto;
    }
    public void setId_assunto(Integer id_assunto) {
        this.id_assunto = id_assunto;
    }
    public String getAssunto() {
        return assunto;
    }
    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }
    public Secretaria getSecretaria() {
        return Secretaria;
    }
    public void setSecretaria(Secretaria secretaria) {
        Secretaria = secretaria;
    }

    public Double getValor_protocolo() {
        return valor_protocolo;
    }

    public void setValor_protocolo(Double valor_protocolo) {
        this.valor_protocolo = valor_protocolo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_assunto == null) ? 0 : id_assunto.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Assunto other = (Assunto) obj;
        if (id_assunto == null) {
            if (other.id_assunto != null)
                return false;
        } else if (!id_assunto.equals(other.id_assunto))
            return false;
        return true;
    }


}
