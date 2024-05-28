package com.proton.controller.resources.redirecionamento;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proton.models.redirecionamento.Redirecionamento;
import com.proton.services.redirecionamento.RedirecionamentoService;
import com.proton.services.user.AuthenticationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/protoon/redirecionamento")
public class RedirecionamentoController {

    @Autowired
    RedirecionamentoService service;

    @Autowired
    AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<Redirecionamento>> findAll(){
        List<Redirecionamento> list = service.findAll();

        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Redirecionamento> findById(@PathVariable Integer id){
        Redirecionamento redirecionamento = service.findById(id);

        return ResponseEntity.ok().body(redirecionamento);
    }

    @GetMapping("/funcionario")
    public ResponseEntity<List<Redirecionamento>> findByFuncionarioId(HttpServletRequest request){
        Integer id_fun = authenticationService.getUserIdFromToken(request);

        return ResponseEntity.ok().body(service.findByIdFuncionario(id_fun));
    }

    @PostMapping("/{id_prot}")
    public ResponseEntity<Redirecionamento> insertByToken(@RequestBody Redirecionamento redirecionamento,
    HttpServletRequest request, @PathVariable Integer id_prot){
        Integer id_fun = authenticationService.getUserIdFromToken(request);

        Redirecionamento redSaved = service.insert(redirecionamento, id_fun, id_prot);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
           .buildAndExpand(redirecionamento.getId()).toUri();
        return ResponseEntity.created(uri).body(redSaved);
    }

    @PutMapping("/by-coordenador/{id_red}")
    public ResponseEntity<Redirecionamento> updateByCoordenador(@RequestBody Redirecionamento redirecionamento,
    @PathVariable Integer id_red, HttpServletRequest request){
        Integer id_fun = authenticationService.getUserIdFromToken(request);
        Redirecionamento redSaved = service.updateByCoordenador(id_red, redirecionamento, id_fun);
        System.out.println(id_red);
        return ResponseEntity.ok().body(redSaved);

    }

    //Atualizar por lote
    @PutMapping("/by-coordenador")
public ResponseEntity<List<Redirecionamento>> updateByCoordenadorList(@RequestBody List<Redirecionamento> redirecionamentos,
    HttpServletRequest request) {
    Integer id_fun = authenticationService.getUserIdFromToken(request);
    List<Redirecionamento> redirecionamentosAtualizados = service.updateByCoordenador(redirecionamentos, id_fun);
    return ResponseEntity.ok().body(redirecionamentosAtualizados);
}
    

}
