package com.proton.services.municipe;

import static com.proton.models.enums.Role.MUNICIPE;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.municipe.Municipe;
import com.proton.models.repositories.MunicipeRepository;
import com.proton.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Component
public class MunicipeService {

    @Autowired // Para que o Spring faça essa injeção de Dependência do Repository
    private MunicipeRepository repository;    

    // Método que retorna uma lista Json com todos os municipes
    public List<Municipe> findAll() {
        return repository.findAll();
    }

    // Método que retorna um objeto Json muncipe com base no id inserido
    public Municipe findById(Integer id) {
        Optional<Municipe> obj = repository.findById(id);
        return obj.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Municipe findByNome(String nome) {
        Optional<Municipe> obj = repository.findByNome(nome);
        return obj.orElse(null);
    }

    // Método que insere um municipe novo no banco de dados, junto com o endereço
    public Municipe insert(Municipe obj) {
        // Faz o relacionamento de municipe e endereço
        Endereco endereco = obj.getEndereco();
        endereco.setMunicipe(obj);
        obj.setEndereco(endereco);

        // Diz a permissão do municipe no código
        obj.setRole(MUNICIPE);

        return repository.save(obj);
    }

    // NÃO VAI DELETAR USUÁRIOS >>>> REGRA DE NEGÓCIO
    // public void delete(Integer id) {
    // repository.deleteById(id);
    // }

    // Método que atualiza as informações do municipe
    public Municipe update(Integer id, Municipe obj) {
        try {
            Municipe entity = repository.getReferenceById(id); // instancia o usuário sem mexer no banco de dados
            updateData(entity, obj);

            return repository.save(entity);
        } catch (EntityNotFoundException e) { //
            throw new ResourceNotFoundException(id);
        }
    }

    private void updateData(Municipe entity, Municipe obj) {
        entity.setNome(obj.getNome());
        entity.setEmail(obj.getEmail());
        entity.setNum_CPF(obj.getNum_CPF());
        entity.setCelular(obj.getCelular());
        entity.setData_nascimento(obj.getData_nascimento());
        entity.setEndereco(obj.getEndereco());
    }

}
