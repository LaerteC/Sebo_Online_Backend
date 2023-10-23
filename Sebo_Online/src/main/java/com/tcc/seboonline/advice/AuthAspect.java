package com.tcc.seboonline.advice;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.exceptions.NotLoggedInException;
import com.tcc.seboonline.exceptions.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class AuthAspect {


    private final HttpServletRequest req;

    public AuthAspect(HttpServletRequest req) {
        this.req = req;
    }

    @Around("@annotation(authorized)")
    public Object authenticate(ProceedingJoinPoint pjp, Authorized authorized) throws Throwable {

        HttpSession session = req.getSession(false);
        if (session == null) {
            throw new UnauthorizedException("Deve estar logado para realizar esta ação");
        }

        if (session.getAttribute("user") == null) {
            throw new UnauthorizedException("Deve estar logado para realizar esta ação");
        }


        return pjp.proceed(pjp.getArgs());

    }
}
