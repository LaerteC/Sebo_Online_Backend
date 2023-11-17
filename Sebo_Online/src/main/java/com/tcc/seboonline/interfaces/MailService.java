package com.tcc.seboonline.interfaces;

import com.tcc.seboonline.modelos.LivroUsuario;
import com.tcc.seboonline.modelos.Usuario;

public interface MailService {

    void sendEmail(String email);

    void sendEmailUsuario(LivroUsuario user, Usuario owner);
}
