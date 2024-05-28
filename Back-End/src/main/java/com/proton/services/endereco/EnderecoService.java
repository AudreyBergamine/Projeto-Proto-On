package com.proton.services.endereco;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proton.models.entities.endereco.Endereco;
import com.proton.models.repositories.EnderecoRepository;

@Component
public class EnderecoService {

     @Autowired // Para que o Spring faça essa injeção de Dependência do Repository
    private EnderecoRepository repository;

    //Método que retorna uma lista Json com todos os enderecos
    public List<Endereco> findAll(){
        return repository.findAll();
    }

    //Método que retorna um objeto Json endereco com base no id inserido
    public Endereco findById(Integer id){
      
        Optional<Endereco> obj = repository.findById(id);
        return obj.get();
    }

    //Método que insere um endereco novo no banco de dados
    public Endereco insert(Endereco obj) {
		return repository.save(obj);
	}

    public void delete(Integer id) {
		repository.deleteById(id);	
	}

  //Método que atualiza as informações do endereco
    public Endereco update(Integer id, Endereco obj) {
		Endereco entity = repository.getReferenceById(id); //instancia o ENDEREÇO sem mexer no banco de dados
		updateData(entity, obj);
			return repository.save(entity);
	}

    private void updateData(Endereco entity, Endereco obj) {
		// TODO Auto-generated method stub
        entity.setNum_cep(obj.getNum_cep());
        entity.setTipo_endereco(obj.getTipo_endereco());
        entity.setLogradouro(obj.getLogradouro());
        entity.setNome_endereco(obj.getNome_endereco());
        entity.setNum_endereco(obj.getNum_endereco());
        entity.setComplemento(obj.getComplemento());
        entity.setBairro(obj.getBairro());
        entity.setCidade(obj.getCidade());
        entity.setEstado(obj.getEstado());
        entity.setPais(obj.getPais());
        
	}
    
}
