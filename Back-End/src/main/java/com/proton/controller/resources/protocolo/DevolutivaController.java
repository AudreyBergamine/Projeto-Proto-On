package com.proton.controller.resources.protocolo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.models.entities.protocolo.Devolutiva;
import com.proton.models.repositories.FuncionarioRepository;
import com.proton.services.protocolo.DevolutivaService;
import com.proton.services.user.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/protoon/devolutiva")
public class DevolutivaController {

    @Autowired
    private DevolutivaService devolutivaService;

    @Autowired
    private FuncionarioRepository fun;

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<Devolutiva>> findAll() {
        List<Devolutiva> devolutivas = devolutivaService.findAll();
        if (!devolutivas.isEmpty()) {
            return ResponseEntity.ok(devolutivas);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Devolutiva> findDevolutivaById(@PathVariable Integer id) {
        Devolutiva devolutiva = devolutivaService.findById(id);
        if (devolutiva != null) {
            return ResponseEntity.ok(devolutiva);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/criar-devolutiva/{id_Protocolo}")
    public ResponseEntity<Devolutiva> insertDevolutiva(@RequestBody Devolutiva devolutiva,
            @PathVariable Integer id_Protocolo,
            HttpServletRequest request) {
        // Extração do ID do funcionário autenticado pelo TOKEN
        Integer id_funcionario = authenticationService.getUserIdFromToken(request);
        Long id_secretaria = fun.findBySecretaria(id_funcionario);
        if (id_funcionario != null) {
            Devolutiva insertDevolutiva = devolutivaService.insert(devolutiva, id_funcionario, id_Protocolo, id_secretaria);
            return ResponseEntity.status(HttpStatus.CREATED).body(insertDevolutiva);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    
    @GetMapping("/devolutiva-protocolo/{id_protocolo}") // retorna todas as devoluções de um protocolo, TODO organizar esse retorno.
    public ResponseEntity<List<Devolutiva>> findDevolutivasByProtocolo(@PathVariable int id_protocolo) {
        List<Devolutiva> devolutivas = devolutivaService.findByIdProtocolo(id_protocolo);
        //System.out.println("VALOR AQUI: " + devolutivas);
        if (devolutivas.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(devolutivas);
    }
}
