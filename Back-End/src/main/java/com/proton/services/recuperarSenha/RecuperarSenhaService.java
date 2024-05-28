package com.proton.services.recuperarSenha;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.proton.models.entities.municipe.Municipe;
import com.proton.models.entities.recuper_senha.RecuperarSenha;
import com.proton.models.repositories.MunicipeRepository;
import com.proton.models.repositories.RecuperarSenhaRepository;
import com.proton.services.email.EmailService;

@Service
public class RecuperarSenhaService {

    @Autowired
    private RecuperarSenhaRepository recuperarSenhaRepository;

    @Autowired
    private MunicipeRepository municipeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String enviarCodigoEmail(String email) {
        Optional<Municipe> municipeBanco = municipeRepository.findByEmail(email);
        if (municipeBanco.isPresent()) {
            Optional<RecuperarSenha> recuperarSenha = recuperarSenhaRepository.findByEmail(email);
            String codigo = UUID.randomUUID().toString();
            if (recuperarSenha.isPresent()) {
                RecuperarSenha rs = recuperarSenha.get();
                rs.setCodigo(codigo);
                rs.setDateSendCodigo(new Date());
                recuperarSenhaRepository.save(rs);
            } else {
                RecuperarSenha novaRecuperarSenha = new RecuperarSenha();
                novaRecuperarSenha.setEmail(email);
                novaRecuperarSenha.setCodigo(codigo);
                novaRecuperarSenha.setDateSendCodigo(new Date());
                recuperarSenhaRepository.save(novaRecuperarSenha);
            }

            String mensagem = "Para recuperar sua senha, use este código\n" + codigo;

            // Envie o email com o código de recuperação
            Future<String> emailSuccess = emailService.enviarEmailTexto(email, "Recuperação de senha", mensagem);
            try {
                String resultadoEnvio = emailSuccess.get(); // Aguarda a conclusão do envio de email e obtém o resultado
                if (resultadoEnvio.equals("Sucesso")) {
                    return "Email enviado com sucesso!";
                } else {
                    return "Falha ao enviar o email!";
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return "Falha ao enviar o email!";
            }
        }
        return "Email não encontrado!";
    }

    public String alterarSenha(RecuperarSenha recuperarSenha) {
        Optional<RecuperarSenha> recuperarSenhaBancoOpt = recuperarSenhaRepository
                .findByEmailAndCodigo(recuperarSenha.getEmail(), recuperarSenha.getCodigo());
        if (recuperarSenhaBancoOpt.isPresent()) {
            RecuperarSenha recuperarSenhaBanco = recuperarSenhaBancoOpt.get();
            Date expiration = new Date(new Date().getTime() - recuperarSenhaBanco.getDateSendCodigo().getTime());
            if ((expiration.getTime() / 1000) < 300) { // 5 minutos para usar o código
                Optional<Municipe> municipeOpt = municipeRepository.findByEmail(recuperarSenha.getEmail());
                if (municipeOpt.isPresent()) {
                    Municipe municipe = municipeOpt.get();
                    municipe.setSenha(passwordEncoder.encode(recuperarSenha.getSenha()));
                    municipeRepository.save(municipe);
                    recuperarSenhaBanco.setCodigo(null);
                    recuperarSenhaBanco.setSenha(passwordEncoder.encode(recuperarSenha.getSenha()));
                    recuperarSenhaRepository.save(recuperarSenhaBanco);
                    return "Senha alterada com sucesso!";
                } else {
                    return "Municipe não encontrado!";
                }
            } else {
                return "Tempo expirado, solicite um novo código!";
            }
        } else {
            return "Email ou código não encontrado!";
        }
    }

}
