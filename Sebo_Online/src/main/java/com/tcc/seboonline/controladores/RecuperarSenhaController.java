package com.tcc.seboonline.controladores;


import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import com.tcc.seboonline.servicos.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api")
public class RecuperarSenhaController {

    private final Logger logger = LoggerFactory.getLogger(RecuperarSenhaController.class);

    @Autowired
    private EmailServiceImpl emailService;

    @PostMapping("/recover-password")
    public ResponseEntity<String> enviarEmailSenha(@RequestBody String email){
        try {
            logger.info("Recebida solicitação para enviar email de recuperação de senha para o email: " + email);

            emailService.sendEmail(email);
            logger.info("Email enviado com sucesso para: " + email);
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "E-mail de recuperação de senha enviado com sucesso.");
            return ResponseEntity.ok(responseMap.toString());


        } catch (Exception e) {
            logger.error("Falha ao enviar o email de recuperação de senha para: " + email, e);

            Map<String, String> responseMapError = new HashMap<>();
            responseMapError.put("error", "Falha ao enviar o e-mail de recuperação de senha.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMapError.toString());

        }
    }
}

