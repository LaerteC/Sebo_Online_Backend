package com.tcc.seboonline.controllers;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.dtos.LoginRequest;
import com.tcc.seboonline.dtos.RegisterRequest;
import com.tcc.seboonline.exceptions.EmailReservedException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.models.User;
import com.tcc.seboonline.services.AuthService;
import com.tcc.seboonline.services.ProfileService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        LOGGER.info("Recebeu uma solicitação de login");

        try {
            User user = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

            session.setAttribute("user", user);

            LOGGER.info("Login efetuado com sucesso");

            return ResponseEntity.ok(user);
        } catch (UserNotFoundException e) {
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
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) {
        LOGGER.info("Recebeu uma solicitação de registro");

        try {
            User created = authService.register(new User(0,
                    registerRequest.getEmail(),
                    registerRequest.getPassword(),
                    registerRequest.getFirstName(),
                    registerRequest.getLastName())
            );

            profileService.registerProfile(created);

            LOGGER.info("Usuário registrado com sucesso");

            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EmailReservedException e) {
            LOGGER.error("E-mail já cadastrado", e);

            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/restore-session")
    public ResponseEntity<Object> restoreSession(HttpSession session) {
        LOGGER.info("Recebeu uma solicitação para restaurar a sessão");

        return ResponseEntity.ok().body(session.getAttribute("user"));
    }
}


