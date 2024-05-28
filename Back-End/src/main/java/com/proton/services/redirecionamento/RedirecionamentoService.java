package com.proton.services.redirecionamento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.enums.Role;
import com.proton.models.enums.StatusRedirecionamento;
import com.proton.models.redirecionamento.Redirecionamento;
import com.proton.models.repositories.FuncionarioRepository;
import com.proton.models.repositories.ProtocoloRepository;
import com.proton.models.repositories.RedirecionamentoRepository;
import com.proton.services.validations.RedirecionamentoValidationService;

@Service
public class RedirecionamentoService {

    @Autowired
    RedirecionamentoRepository redirecionamentoRepository;

    @Autowired
    FuncionarioRepository funcionarioRepository;

    @Autowired
    ProtocoloRepository protocoloRepository;

    @Autowired
    RedirecionamentoValidationService redirecionamentoValidationService;

    public List<Redirecionamento> findAll() {
		return redirecionamentoRepository.findAll();
	}

      public List<Redirecionamento> findByIdFuncionario(Integer id) {
        // Encontre o funcionário pelo ID
        Funcionario funcionario = funcionarioRepository.getReferenceById(id);
        // Verifique se o funcionário foi encontrado
   
            // Se sim, retorne todos os redirecionamentos associados a esse funcionário
            return redirecionamentoRepository.findByFuncionario(funcionario);
        
    }


    
    public Redirecionamento findById(Integer id) {
		return redirecionamentoRepository.getReferenceById(id);
	}

    public Redirecionamento insert(Redirecionamento redirecionamento, Integer id_fun, Integer id_prot){
        
        Funcionario funcionario = funcionarioRepository.getReferenceById(id_fun);
        Protocolo protocolo = protocoloRepository.getReferenceById(id_prot);

        redirecionamento.setFuncionario(funcionario);
        redirecionamento.setProtocolo(protocolo);
        redirecionamentoValidationService.validateRedirecionamento(this.findAll(),redirecionamento);
        redirecionamento.setDtSolicitacao(LocalDateTime.now().withNano(0));
        
        //Se a role é de FUNCIONARIO
        redirecionamento.setDescricao("Redirecionamento solicitado por "+funcionario.getNome()+
        ", número do protocolo: "+protocolo.getNumero_protocolo());
        if(funcionario.getRole().equals(Role.FUNCIONARIO)){

            redirecionamento.setStatusRedirecionamento(StatusRedirecionamento.ANDAMENTO);

        //Se a role é de COORDENADOR
        }else if(funcionario.getRole().equals(Role.COORDENADOR)){
            
            redirecionamento.setStatusRedirecionamento(StatusRedirecionamento.APROVADO);
            redirecionamento.setDtConfirmacao(LocalDateTime.now().withNano(0));
        }

        return redirecionamentoRepository.save(redirecionamento);
    }

    public Redirecionamento updateByCoordenador(Integer id_red, Redirecionamento obj, Integer id_fun){
        Redirecionamento entity = redirecionamentoRepository.getReferenceById(id_red);
        Funcionario funcionario = funcionarioRepository.getReferenceById(id_fun);
        updateData(entity, obj, funcionario);
        return redirecionamentoRepository.save(entity);
    }


    //Atualizar por lote
    public List<Redirecionamento> updateByCoordenador(List<Redirecionamento> redirecionamentos, Integer id_fun) {
    List<Redirecionamento> redirecionamentosAtualizados = new ArrayList<>();
    for (Redirecionamento redirecionamento : redirecionamentos) {
        Redirecionamento entity = redirecionamentoRepository.getReferenceById(redirecionamento.getId());
        Funcionario funcionario = funcionarioRepository.getReferenceById(id_fun);
        updateData(entity, redirecionamento, funcionario);
        redirecionamentosAtualizados.add(redirecionamentoRepository.save(entity));
    }
    return redirecionamentosAtualizados;
}

    public void updateData(Redirecionamento entity, Redirecionamento obj, Funcionario funcionario){
        entity.setStatusRedirecionamento(obj.getStatusRedirecionamento());
        entity.setDescricao(entity.getDescricao()+" Atualizado pelo coordernador: "+funcionario.getNome()+ ". No horário: "+LocalDateTime.now().withNano(0));
        entity.setDtConfirmacao(LocalDateTime.now().withNano(0));
    }
}
