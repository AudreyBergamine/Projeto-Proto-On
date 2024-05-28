package com.proton.models.entities.secretaria;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.protocolo.Protocolo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;


@Entity
@Table(name = "secretaria")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Ignora propriedades Hibernate durante a serialização
public class Secretaria implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id_secretaria;
	
	private String nome_secretaria;
	private String nome_responsavel;
	private String email;
	private String senha;
	

	// @OneToOne(cascade = CascadeType.ALL)
	// @JoinColumn(name = "id_enderecoFK", referencedColumnName = "id_endereco")
	// private Endereco endereco;

	@ManyToOne
	@JoinColumn(name = "id_enderecoFK", referencedColumnName = "id_endereco")
	private Endereco endereco;
    
	// @JsonIgnore //Serve para evitar o loop que gera em um relacionamento de banco de dados
	// @OneToMany(mappedBy = "secretaria") //Aqui serve para acessar as orders
	// private List<Departamento> departamentos = new ArrayList<>(); //Quando se trabalha com uma coleção, só se usa os gets (não se usa set)

	@JsonIgnore //Serve para evitar o loop que gera em um relacionamento de banco de dados
	@OneToMany(mappedBy = "secretaria") //Aqui serve para acessar as orders
	private List<Funcionario> funcionarios = new ArrayList<>(); //Quando se trabalha com uma coleção, só se usa os gets (não se usa set)

	@JsonIgnore //Serve para evitar o loop que gera em um relacionamento de banco de dados
	@OneToMany(mappedBy = "secretaria") //Aqui serve para acessar as orders
	private List<Protocolo> protocolos = new ArrayList<>(); //Quando se trabalha com uma coleção, só se usa os gets (não se usa set)


	public Secretaria(){
		
	}

	public Secretaria(Long id_secretaria, String nome_secretaria, String nome_responsavel, String email, String senha, 
			Endereco id_enderecoFK ) {
		super();
		this.id_secretaria = id_secretaria;
		this.nome_secretaria = nome_secretaria;
		this.nome_responsavel = nome_responsavel;
		this.email = email;
		this.senha = senha;	
		this.endereco = id_enderecoFK;
	}

	public Long getId_secretaria() {
		return id_secretaria;
	}

	

	public List<Funcionario> getFuncionarios() {
		return funcionarios;
	}
	public List<Protocolo> getProtocolos(){
		return protocolos;
	}

	public void setProtocolos(List<Protocolo> protocolos){
		this.protocolos = protocolos;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	// public List<Departamento> getDepartamentos() {
	// 	return departamentos;
	// }

	public void setId_secretaria(Long id_secretaria) {
		this.id_secretaria = id_secretaria;
	}

	public String getNome_secretaria() {
		return nome_secretaria;
	}

	public void setNome_secretaria(String nome_secretaria) {
		this.nome_secretaria = nome_secretaria;
	}

	public String getNome_responsavel() {
		return nome_responsavel;
	}

	public void setNome_responsavel(String nome_responsavel) {
		this.nome_responsavel = nome_responsavel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}



	@Override
	public int hashCode() {
		return Objects.hash(id_secretaria);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Secretaria other = (Secretaria) obj;
		return Objects.equals(id_secretaria, other.id_secretaria);
	}
	
	
}
