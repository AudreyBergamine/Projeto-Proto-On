package com.proton.controller.resources.protocolo;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proton.models.entities.Log;
import com.proton.models.entities.endereco.Endereco;
import com.proton.models.entities.funcionario.Funcionario;
import com.proton.models.entities.municipe.Municipe;
import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.models.repositories.MunicipeRepository;
import com.proton.models.repositories.SecretariaRepository;
import com.proton.models.repositories.EnderecoRepository;
import com.proton.models.repositories.FuncionarioRepository;
import com.proton.models.repositories.LogRepository;
import com.proton.models.repositories.ProtocoloRepository;
import com.proton.services.protocolo.ProtocoloService;
import com.proton.services.user.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Optional;

@RestController
@RequestMapping(value = "/protoon/protocolo")
public class ProtocoloController {

    @Autowired
    private ProtocoloService protocoloService;

    @Autowired
    private ProtocoloRepository protocoloRepository;

    @Autowired
    private MunicipeRepository municipeRepository;

    @Autowired
    private SecretariaRepository secretariaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    Log log = new Log();

    @GetMapping(value = "/todos-protocolos") // Adicionando a anotação GetMapping para o método findAll
    public ResponseEntity<List<Protocolo>> findAll() {
        List<Protocolo> list = protocoloService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{numero_protocolo}") // Pesquisa por numero de protocolo
    public ResponseEntity<Protocolo> findByNumeroProtocolo(@PathVariable String numero_protocolo) {
        Optional<Protocolo> protocoloOptional = protocoloRepository.findByNumeroProtocolo(numero_protocolo);
        if (protocoloOptional.isPresent()) {
            Protocolo obj = protocoloService.findByNumero_protocolo(numero_protocolo);
            return ResponseEntity.ok().body(obj);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/pesquisar-municipe/{nomeMunicipe}") // Pesquisar pelo nome do municipe
    public ResponseEntity<List<Protocolo>> findByNomeMunicipe(@PathVariable String nomeMunicipe) {
        List<Protocolo> protocolos = protocoloService.findByNomeMunicipe(nomeMunicipe);
        if (!protocolos.isEmpty()) {
            return ResponseEntity.ok().body(protocolos); // Retorna os protocolos encontrados
        } else {
            return ResponseEntity.notFound().build(); // Retorna status 404 se nenhum protocolo for encontrado
        }
    }

    @GetMapping(value = "/pesquisar-id/{id}") // pesquisar pelo ID
    public ResponseEntity<Protocolo> findById(@PathVariable Integer id) {
        Protocolo obj = protocoloService.findById(id);
        return ResponseEntity.ok().body(obj);// retorna UM protocolo
    }

    @PostMapping(value = "/abrir-protocolos/{id_s}") // Gera novos protocolos
    public ResponseEntity<Protocolo> insertByToken(@RequestBody Protocolo protocolo, @PathVariable Long id_s,
            HttpServletRequest request) {
        Integer id_m = authenticationService.getUserIdFromToken(request);
        Municipe mun = municipeRepository.getReferenceById(id_m);
        Secretaria sec = secretariaRepository.getReferenceById(id_s);
        Endereco end = enderecoRepository.getReferenceById(mun.getEndereco().getId_endereco());
        String numeroProtocolo = protocoloService.gerarNumeroProtocolo();
        protocolo.setNumero_protocolo(numeroProtocolo);
        protocolo.setMunicipe(mun);
        protocolo.setEndereco(end);
        protocolo.setSecretaria(sec);
        protocoloRepository.save(protocolo);

        String mensagemLog = String.format("Foi Registrado um novo protocolo: " + protocolo.getNumero_protocolo() + " em %s",
                LocalDateTime.now().format(formatter));

        log.setMensagem(mensagemLog);
        logRepository.save(log);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(protocolo.getId_protocolo()).toUri();
        return ResponseEntity.created(uri).body(protocolo);
    }

    @PostMapping(value = "/abrir-protocolos/{id_m}/{id_s}") // Gera novos protocolos
    public ResponseEntity<Protocolo> insert(@RequestBody Protocolo protocolo, @PathVariable Integer id_m,
            @PathVariable Long id_s) {

        Protocolo prot = protocoloService.insert(protocolo, id_m, id_s);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(protocolo.getId_protocolo()).toUri();
        return ResponseEntity.created(uri).body(prot);
    }

    @PutMapping("/alterar-protocolos/status/{numero_protocolo}") // Altera os protocolos (TODO REVER ISSO DEPOIS)
    public ResponseEntity<Protocolo> update(@PathVariable String numero_protocolo, @RequestBody Protocolo protocolo,
            HttpServletRequest request) {
        Integer id_funcionario = authenticationService.getUserIdFromToken(request);
        Funcionario funcionario = funcionarioRepository.findById(id_funcionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Protocolo obj = protocoloService.updateStatus(numero_protocolo, protocolo, funcionario.getNome());
        return ResponseEntity.ok(obj);
    }

    @PutMapping("/alterar-protocolos/departamento/{numero_protocolo}") // Altera os protocolos (TODO REVER ISSO DEPOIS)
    public ResponseEntity<Protocolo> updateRedirect(@PathVariable String numero_protocolo,
            @RequestBody Protocolo protocolo, HttpServletRequest request) {

        Integer id_funcionario = authenticationService.getUserIdFromToken(request);
        Funcionario funcionario = funcionarioRepository.findById(id_funcionario)
                .orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

        Protocolo obj = protocoloService.updateRedirect(numero_protocolo, protocolo, funcionario.getNome());
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "/meus-protocolos/bytoken") // Pesquisa os protocolos do munipe logado
    public ResponseEntity<List<Protocolo>> findByIdMunicipe(HttpServletRequest request) {
        // Extração do ID do munícipe autenticado pelo TOKEN (Atualização para a
        // segurança do site)
        // Validação para ver se o TOKEN foi recebido msm
        Integer id = authenticationService.getUserIdFromToken(request);
        if (id != null) {
            Optional<Municipe> municipeOptional = municipeRepository.findById(id);
            if (municipeOptional.isPresent()) {
                Municipe municipe = municipeOptional.get();
                // Usa o ID do municipe recuperado ali em cima para buscar os protocolos, igual
                // antes
                List<Protocolo> protocolos = protocoloService.findByMunicipe(municipe);
                return ResponseEntity.ok().body(protocolos);// retorna VARIOS protocolos do MUNICIPE LOGADO
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
