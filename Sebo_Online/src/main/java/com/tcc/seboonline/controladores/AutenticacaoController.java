package com.tcc.seboonline.controladores;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.DTOs.RequisicaoLoginDTO;
import com.tcc.seboonline.DTOs.RegistroDTO;
import com.tcc.seboonline.excecoes.EmailReservadoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.servicos.AuthService;
import com.tcc.seboonline.servicos.PerfilUsuarioService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    private static final Logger LOGGER = LogManager.getLogger(AutenticacaoController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private PerfilUsuarioService profileService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody RequisicaoLoginDTO loginRequest, HttpSession session) {
        LOGGER.info("Recebeu uma solicitação de login");

        try {
            Usuario user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

            session.setAttribute("user", user);

            LOGGER.info("Login efetuado com sucesso");

            return ResponseEntity.ok(user);
        } catch (UsuarioNEncontradoException e) {
            LOGGER.error("Usuário não encontrado", e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.removeAttribute("user");

        LOGGER.info("Logout efetuado com sucesso");

        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegistroDTO registerRequest) {
        LOGGER.info("Recebeu uma solicitação de registro");

        try {
            Usuario created = authService.register(new Usuario(0,
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName())
            );

            profileService.registerProfile(created);

            LOGGER.info("Usuário registrado com sucesso");

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EmailReservadoException e) {
            LOGGER.error("E-mail já cadastrado", e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/restore-session")
    public ResponseEntity<Object> restoreSession(HttpSession session) {
        LOGGER.info("Recebeu uma solicitação para restaurar a sessão");

        return ResponseEntity.ok().body(session.getAttribute("user"));
    }
}


