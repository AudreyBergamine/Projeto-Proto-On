package com.proton.controller.resources.assunto;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.models.entities.assunto.Assunto;
import com.proton.services.Assunto.AssuntoService;

@RestController
@RequestMapping(value = "/protoon/assuntos")
public class AssuntoController {

    @Autowired
    private AssuntoService assuntoService;

    @GetMapping
    public ResponseEntity<List<Assunto>> findAll() {
        List<Assunto> list = assuntoService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Assunto> findById(@PathVariable Integer id) {
        Assunto obj = assuntoService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping(value = "/registrar-assunto")
    public ResponseEntity<Assunto> insert(@RequestBody Assunto assunto) {
        assuntoService.create(assunto);
        return ResponseEntity.ok(assunto);
    }

    @PutMapping("/alterar-assunto/{id}")
    public ResponseEntity<Assunto> update(@PathVariable Integer id, @RequestBody Assunto protocolo) {
        Assunto obj = assuntoService.update(id, protocolo);
        return ResponseEntity.ok(obj);
    }

}
