package com.tcc.seboonline.advice;

import com.tcc.seboonline.excecoes.NaoLogadoException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(RestExceptionHandler.class);


    @ExceptionHandler(NaoLogadoException.class)
    public ResponseEntity<Object> handleNotLoggedInException(HttpServletRequest request, NaoLogadoException notLoggedInException) {

        // Registra um log de info
        LOGGER.info("Usuário não está logado. Enviando resposta HTTP 401 Unauthorized");

        String errorMessage = "Deve estar logado para realizar esta ação";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMessage);
    }
}
