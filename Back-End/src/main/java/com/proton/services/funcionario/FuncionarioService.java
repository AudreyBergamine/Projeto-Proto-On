package com.proton.services.funcionario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.repositories.FuncionarioRepository;
import com.proton.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Component
@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository repository;

    public List<Funcionario> findAll() {
        return repository.findAll();
    }

    public Funcionario findById(Integer id) {
        Optional<Funcionario> optionalFuncionario = repository.findById(id);
        return optionalFuncionario.orElse(null); // Retorna null se não encontrar o funcionário
    }
    //TODO: Verificar se o método insert está correto!
    public Funcionario insert(Funcionario funcionario) {
        return repository.save(funcionario);
    }

  public Funcionario update(Integer id, Secretaria secretaria, Funcionario obj) {
    try {
        Funcionario entity = repository.getReferenceById(id); //instancia o usuário sem mexer no banco de dados
        updateData(entity, secretaria, obj);
        return repository.save(entity);
    } catch (EntityNotFoundException e) { //
            throw new ResourceNotFoundException(id);
        }
}


  private void updateData(Funcionario entity, Secretaria secretaria, Funcionario obj) {
        entity.setNome(obj.getNome());
        entity.setEmail(obj.getEmail());
        entity.setRole(obj.getRole());
        entity.setNumCPF(obj.getNum_CPF());
        entity.setDataNascimento(obj.getData_nascimento());
        entity.setCelular(obj.getCelular());
        entity.setNumTelefoneFixo(obj.getNumTelefoneFixo());
        entity.setEndereco(obj.getEndereco());
        entity.setSecretaria(secretaria);

     }

     public Funcionario updateByToken(Integer id, Funcionario obj) {
        try {
            Funcionario entity = repository.getReferenceById(id); //instancia o usuário sem mexer no banco de dados
            updateDataByToken(entity, obj);
            return repository.save(entity);
        } catch (EntityNotFoundException e) { //
                throw new ResourceNotFoundException(id);
            }
    }
    
    
      private void updateDataByToken(Funcionario entity, Funcionario obj) {
            entity.setNome(obj.getNome());
            entity.setEmail(obj.getEmail());
            entity.setRole(obj.getRole());
            entity.setNumCPF(obj.getNum_CPF());
            entity.setDataNascimento(obj.getData_nascimento());
            entity.setCelular(obj.getCelular());
            entity.setNumTelefoneFixo(obj.getNumTelefoneFixo());
            entity.setEndereco(obj.getEndereco());
            entity.setSecretaria(obj.getSecretaria());
    
         }

}