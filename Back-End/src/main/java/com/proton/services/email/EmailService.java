package com.proton.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import java.util.concurrent.Future;


@SuppressWarnings("deprecation")
@Service
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public String sendEmail(String toEmail,
    String subject, String body){
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("vagnerimperador16@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
    
            emailSender.send(message);
            System.out.println("Sucesso");
            return "Sucesso";
            
        } catch (Exception e) {
            e.printStackTrace();
            return "Fracasso";
        }
      

    }
    @Async
    public Future<String> enviarEmailTexto(String destinatario, String titulo, String mensagem) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(remetente);
            message.setTo(destinatario);
            message.setSubject(titulo);
            message.setText(mensagem);
            
            // Envie o email
            emailSender.send(message);

            return new AsyncResult<>("Sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            return new AsyncResult<>("Falha");
        }
    }
}

