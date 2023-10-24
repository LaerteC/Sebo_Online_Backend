package com.tcc.seboonline.advice;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.excecoes.NaoAutorizadoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class AuthAspect {


    private final HttpServletRequest req;

    public AuthAspect(HttpServletRequest req) {
        this.req = req;
    }

    @Around("@annotation(authorized)")
    public Object authenticate(ProceedingJoinPoint pjp, AutorizacaoUsuario authorized) throws Throwable {

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new NaoAutorizadoException("Deve estar logado para realizar esta ação");
        }

        if (session.getAttribute("user") == null) {
            throw new NaoAutorizadoException("Deve estar logado para realizar esta ação");
        }


        return pjp.proceed(pjp.getArgs());

    }
}
