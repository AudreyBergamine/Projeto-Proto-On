package com.proton.controller.resources.recuperarSenha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proton.models.entities.recuper_senha.RecuperarSenha;
import com.proton.services.recuperarSenha.RecuperarSenhaService;

@RestController
@RequestMapping(value = "/protoon/municipe/municipes/recuperarSenha")
public class RecuperarSenhaController {

    @Autowired
    private RecuperarSenhaService recuperarSenhaService;

    @PostMapping("/")
    public String requestPasswordReset(@RequestBody RecuperarSenha recuperarSenha) {
        return recuperarSenhaService.enviarCodigoEmail(recuperarSenha.getEmail());
    }
    
    @PostMapping("/novaSenha")
    public String requestNovaSenha(@RequestBody RecuperarSenha recuperarSenha) {
        return recuperarSenhaService.alterarSenha(recuperarSenha);
    }
}
