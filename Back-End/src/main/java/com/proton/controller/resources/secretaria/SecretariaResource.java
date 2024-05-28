package com.proton.controller.resources.secretaria;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.models.entities.protocolo.Protocolo;
import com.proton.models.entities.secretaria.Secretaria;
import com.proton.services.secretaria.SecretariaService;

@RestController
@RequestMapping(value = "protoon/secretaria")
public class SecretariaResource {
    
    @Autowired
    private SecretariaService secretariaService;
    
    @GetMapping // Adicionando a anotação GetMapping para o método findAll
    public ResponseEntity<List<Secretaria>> findAll() {
        List<Secretaria> list = secretariaService.findAll();
        return ResponseEntity.ok().body(list);
    }
    
    @GetMapping(value = "/{id}") // Adicionando o caminho da variável id
    public ResponseEntity<Secretaria> findById(@PathVariable Long id) {
        Secretaria obj = secretariaService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/protocolos/{id}")
    public ResponseEntity<List<Protocolo>> findProtocolosBySecretariaId(@PathVariable Long id){
        Secretaria sec = secretariaService.findById(id);
        return ResponseEntity.ok().body(sec.getProtocolos());

    }
}

