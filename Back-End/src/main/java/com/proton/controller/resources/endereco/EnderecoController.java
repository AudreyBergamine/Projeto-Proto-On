package com.proton.controller.resources.endereco;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.proton.models.entities.endereco.Endereco;
import com.proton.services.endereco.EnderecoService;

@RestController
@RequestMapping(value = "protoon/municipe/endereco")
@CrossOrigin(origins = "http://localhost:3000")
public class EnderecoController {
    @Autowired // Para que o Spring faça essa injeção de Dependência do Service
    private EnderecoService service; // Dependência para o Service

    // Método que responde á requisição do tipo GET do HTTP
     @GetMapping()
    public ResponseEntity<List<Endereco>> findAll(){
        List<Endereco> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}") // A requisição vai aceitar um ID dentro do URL
    public ResponseEntity<Endereco> findById(@PathVariable Integer id){
        Endereco obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    // Método que responde á requisição do tipo POST do HTTP
    @PostMapping
    public ResponseEntity<Endereco> insert(@RequestBody Endereco obj){
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId_endereco()).toUri();
		return ResponseEntity.created(uri).body(obj); //Código 201
	}

    // TODO: VAI PODER EXCLUIR ENDEREÇO?? Pois iria perder o histórico de reclamações >>>> REGRA DE NEGÓCIO 
    @DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build(); //Resposta para sem conteúdo, código 204
	}

    @PutMapping(value = "{id}") // A requisição vai aceitar um ID dentro do URL
	public ResponseEntity<Endereco> update(@PathVariable Integer id, @RequestBody Endereco obj){
		obj = service.update(id, obj);
		return ResponseEntity.ok(obj);
	}
    

    
}
