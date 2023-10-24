package com.tcc.seboonline.servicos;

import com.tcc.seboonline.excecoes.EmailReservadoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.modelos.Usuario;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UsuarioService userService;

    public AuthService(UsuarioService userService) {
        this.userService = userService;
    }

    public Optional<Usuario> findByCredentials(String email, String password) {
        return userService.findByCredentials(email, password);
    }

    public Usuario login(String login, String password) throws UsuarioNEncontradoException {
        Optional<Usuario> optional = findByCredentials(login, password);

        if(!optional.isPresent()) 
            throw new UsuarioNEncontradoException("Um usuário com essas credenciais não foi encontrado.");

        return optional.get();
    }

    public Usuario register(Usuario user) throws EmailReservadoException {
        return userService.save(user);
    }
}
