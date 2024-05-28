package com.proton.models.entities.endereco;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.municipe.Municipe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

//O anotação @Entity, diz que é uma tabela do banco de dados. Já @Table, é para incluir o nome da tabela
@Entity
@Table(name = "endereco")
public class Endereco implements Serializable{


    //Implements Serializable é uma interface que transforma os dados do objeto em dados bytes, para transmitir pela web.
    private static final long serialVersionUID = 1L;

    //O atributo id_municipe, precisa ter as anotações abaixo para dizer que é um id, autoincrement e o seu nome
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_endereco")
    private Integer id_endereco;
    
    


    @Column(name = "tipo_endereco", nullable = false)
    private String tipo_endereco; 

    @Column(name = "num_cep", nullable = false)
    private String num_cep; 

    @Column(name = "logradouro", nullable = false)
    private String logradouro; 

    @Column(name = "nome_endereco", nullable = true)
    private String nome_endereco; 

    @Column(name = "num_endereco", nullable = false)
    private String num_endereco;
    
    @Column(name = "complemento", nullable = false)
    private String complemento;

    @Column(name = "bairro", nullable = false)
    private String bairro;

    @Column(name = "cidade", nullable = false)
    private String cidade;

    @Column(name = "estado", nullable = false)
    private String estado;

    @Column(name = "pais", nullable = false)
    private String pais;

    //A anotação @JsonIgnore serve para evitar um loop quando se pesquisa uma entidade relacionada
    //O OneToOne, está mapeado pela atributo "endereco" que está em municipe
    @JsonIgnore
    @OneToOne(mappedBy = "endereco")
    private Municipe municipe;

    @JsonIgnore
    @OneToOne(mappedBy = "endereco")
    private Funcionario funcionario;

    // @JsonIgnore
    // @OneToOne(mappedBy = "endereco")
    // private Departamento departamento;

    // @JsonIgnore
    // @OneToOne(mappedBy = "endereco")
    // private Secretaria secretaria;

    public Endereco(){

    }

    public Endereco(Integer id_endereco, String tipo_endereco, String num_cep, String logradouro, String nome_endereco,
            String num_endereco, String complemento, String bairro, String cidade, String estado, String pais) {
        this.id_endereco = id_endereco;
        this.tipo_endereco = tipo_endereco;
        this.num_cep = num_cep;
        this.logradouro = logradouro;
        this.nome_endereco = nome_endereco;
        this.num_endereco = num_endereco;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.pais = pais;
    }


    public Integer getId_endereco() {
        return id_endereco;
    }

    public void setId_endereco(Integer id_endereco) {
        this.id_endereco = id_endereco;
    }

    public String getTipo_endereco() {
        return tipo_endereco;
    }

    public void setTipo_endereco(String tipo_endereco) {
        this.tipo_endereco = tipo_endereco;
    }

    public String getNum_cep() {
        return num_cep;
    }

    public void setNum_cep(String num_cep) {
        this.num_cep = num_cep;
    }
    
    public Municipe getMunicipe() {
        return municipe;
    }

    public void setMunicipe(Municipe municipe) {
        this.municipe = municipe;
    }

    

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    // public Departamento getDepartamento() {
    //     return departamento;
    // }

    // public void setDepartamento(Departamento departamento) {
    //     this.departamento = departamento;
    // }

    // public Secretaria getSecretaria() {
    //     return secretaria;
    // }

    // public void setSecretaria(Secretaria secretaria) {
    //     this.secretaria = secretaria;
    // }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNome_endereco() {
        return nome_endereco;
    }

    public void setNome_endereco(String nome_endereco) {
        this.nome_endereco = nome_endereco;
    }

    public String getNum_endereco() {
        return num_endereco;
    }

    public void setNum_endereco(String num_endereco) {
        this.num_endereco = num_endereco;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    //A anotação @OneToOne é para dizer que é uma relação 1 para 1 e o @JoinColumn diz que é uma chave estrangeira
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id_endereco == null) ? 0 : id_endereco.hashCode());
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
        Endereco other = (Endereco) obj;
        if (id_endereco == null) {
            if (other.id_endereco != null)
                return false;
        } else if (!id_endereco.equals(other.id_endereco))
            return false;
        return true;
    }

    



    
    
}
