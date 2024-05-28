// //SERVICE LAYER (RESOURCER -->SERVICE LAYER(AQUI) --> REPOSITORY
// package com.proton.services.departamento;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.proton.models.entities.Departamento;
// import com.proton.models.repositories.DepartamentoRepository;



// // Esta classe representa um serviço responsável por operações relacionadas a secretaria
// @Service
// public class DepartamentoService {
	
// 	@Autowired
// 	private DepartamentoRepository repository; // Repositório de dados para acesso a secretaria
	
// 	// Método para encontrar todos as secretarias
// 	public List<Departamento> findAll(){
// 		return repository.findAll(); // Retorna todos as secretarias armazenadas no banco de dados
// 	}
	
// 	public Departamento findById(Long id) {
// 		Optional <Departamento> obj = repository.findById(id);
// 		return obj.get();
// 	}
// }