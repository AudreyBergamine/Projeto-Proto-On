package com.proton.models.entities.municipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

//O anotação @Entity, diz que é uma tabela do banco de dados. Já @Table, é para incluir o nome da tabela
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //Para funcionar o protocolo precisa dessa notação, verificar se ela não tem impacto em outra entidade (-- TODO -- ZEZINHO)
public class Municipe extends User {

    //Implements Serializable é uma interface que transforma os dados do objeto em dados bytes, para transmitir pela web.
    //O Serializable já existe em User... em alguma annotation!

    //O atributo id_municipe, precisa ter as anotações abaixo para dizer que é um id, autoincrement e o seu nome

    @Column(name = "num_CPF", nullable = false, unique = true)
    private String num_CPF;
    
    @Column(name = "celular", nullable = false)
    private String celular;

    //@JsonFormat(pattern="dd-MM-yyyy")
    @Column(name = "data_nascimento", nullable = true)
    private LocalDate data_nascimento;
    
    //A anotação @OneToOne é para dizer que é uma relação 1 para 1 e o @JoinColumn diz que é uma chave estrangeira
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_enderecoFK", referencedColumnName = "id_endereco")
    private Endereco endereco;

    @JsonIgnore //Serve para evitar o loop que gera em um relacionamento de banco de dados
	@OneToMany(mappedBy = "municipe") //Aqui serve para acessar as orders
	private List<Protocolo> protocolos = new ArrayList<>(); //Quando se trabalha com uma coleção, só se usa os gets (não se usa set)
 
    //É necessário ter dois construtores, um padrão e um com todos os atributos do municipe
    public Municipe(String nome_municipe, String email, String senha, String num_CPF, String celular, LocalDate data_nascimento,
            Endereco id_enderecoFK) {
        this.setNome(nome_municipe);
        this.setEmail(email);
        this.setSenha(senha);
        this.num_CPF = num_CPF;
        this.celular = celular;
        this.data_nascimento = data_nascimento;
        this.endereco = id_enderecoFK;
    }


    public Endereco getEndereco() {
        return endereco;
    }
 
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Integer getId_municipe() {
        return getId();
    }   

    public String getNum_CPF() {
        return num_CPF;
    }

    public void setNum_CPF(String num_CPF) {
        this.num_CPF = num_CPF;
    }
    
    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public List<Protocolo> getProtocolos(){
        return protocolos;
    }


    //O Hashcode e Equals serve para poder se comparar 2 ids diferentes
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId_municipe() == null) ? 0 : getId_municipe().hashCode());
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
        Municipe other = (Municipe) obj;
        if (getId_municipe() == null) {
            if (other.getId_municipe() != null)
                return false;
        } else if (!getId_municipe().equals(other.getId_municipe()))
            return false;
        return true;
    }    
}


