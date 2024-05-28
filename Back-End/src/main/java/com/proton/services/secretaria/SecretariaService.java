//SERVICE LAYER (RESOURCER -->SERVICE LAYER(AQUI) --> REPOSITORY
package com.proton.services.secretaria;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.repositories.SecretariaRepository;



// Esta classe representa um serviço responsável por operações relacionadas a secretaria
@Service
public class SecretariaService {
	
	@Autowired
	private SecretariaRepository repository; // Repositório de dados para acesso a secretaria
	
	// Método para encontrar todos as secretarias
	public List<Secretaria> findAll(){
		return repository.findAll(); // Retorna todos as secretarias armazenadas no banco de dados
	}
	
	public Secretaria findById(Long id) {
		Optional <Secretaria> obj = repository.findById(id);
		return obj.get();
	}

	public List<Protocolo> findSecretariaProtocolos(Secretaria secretaria){
		return secretaria.getProtocolos();
	}
}