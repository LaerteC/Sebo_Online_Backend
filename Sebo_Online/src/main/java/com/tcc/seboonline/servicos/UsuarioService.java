package com.tcc.seboonline.servicos;

import com.tcc.seboonline.excecoes.EmailReservadoException;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository userRepository;

    public Optional<Usuario> findByCredentials(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public Usuario save(Usuario user) throws EmailReservadoException {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EmailReservadoException("O " + user.getEmail() + " e-mail já foi usado.");

        return userRepository.save(user);
    }

    public Optional<Usuario> findById(int id) {
        return this.userRepository.findById(id);
    }
}
