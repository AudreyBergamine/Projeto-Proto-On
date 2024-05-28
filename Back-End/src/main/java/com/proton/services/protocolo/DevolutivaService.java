//SERVICE LAYER (RESOURCER -->SERVICE LAYER(AQUI) --> REPOSITORY
package com.proton.services.protocolo;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proton.models.entities.Log;
import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.protocolo.Devolutiva;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.repositories.DevolutivaRepository;
import com.proton.models.repositories.FuncionarioRepository;
import com.proton.models.repositories.LogRepository;
import com.proton.models.repositories.ProtocoloRepository;
import com.proton.models.repositories.SecretariaRepository;

@Service
public class DevolutivaService {

    @Autowired
    private DevolutivaRepository devolutivaRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private ProtocoloRepository protocoloRepository;

    @Autowired
    private SecretariaRepository secretariaRepository;

    @Autowired
	private LogRepository logRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Método para encontrar TODOS os Devolutivas
    public List<Devolutiva> findAll() {
        return devolutivaRepository.findAll();
    }

    // Método para encontrar Devolutiva pelo id
    public Devolutiva findById(Integer id) {
        Optional<Devolutiva> obj = devolutivaRepository.findById(id);
        return obj.get();
    }

    public Devolutiva insert(Devolutiva devolutiva, Integer id_funcionario, Integer id_protocolo, Long id_secretaria) {
        // Validação de funcionario e protocolo
        Funcionario funcionario = funcionarioRepository.findById(id_funcionario)
                .orElseThrow(
                        () -> new IllegalArgumentException("Funcionário não encontrado com o ID: " + id_funcionario));
        Protocolo protocolo = protocoloRepository.findById(id_protocolo)
                .orElseThrow(() -> new IllegalArgumentException("Protocolo não encontrado com o ID: " + id_protocolo));
        Secretaria secretaria = secretariaRepository.findById(id_secretaria)
                .orElseThrow(
                        () -> new IllegalArgumentException("Secretaria não encontrada com o ID: " + id_secretaria));

        Instant momentoAtual = Instant.now();
        devolutiva.setMomento_devolutiva(momentoAtual);
        devolutiva.setId_funcionario(funcionario);
        devolutiva.setId_protocolo(protocolo);
        devolutiva.setId_secretaria(secretaria);

        String mensagemLog = String.format(funcionario.getNome() + " fez uma devolutiva do protocolo " + protocolo.getNumero_protocolo() + " em " + LocalDateTime.now().format(formatter));
        System.out.println(mensagemLog);

        Log log = new Log();
        log.setMensagem(mensagemLog);
        logRepository.save(log);

        return devolutivaRepository.save(devolutiva);
    }

    public List<Devolutiva> findByIdProtocolo(int id_protocolo) {
        return devolutivaRepository.findByIdProtocolo(id_protocolo);
    }

}
