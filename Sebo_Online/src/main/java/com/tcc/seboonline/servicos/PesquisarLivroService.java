package com.tcc.seboonline.servicos;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.UsuarioNaoEncontradoException;
import com.tcc.seboonline.modelos.PerfilUsuario;

@Service
public class PesquisarLivroService {

    @Autowired
    private PerfilUsuarioRepository profileRepository;

    @Autowired
    private UsuarioRepository userRepository;

    /**
     * Obtém uma lista de perfis de usuário com um limite específico.
     *
     * @param limitResponseLength O limite de perfis a serem retornados.
     * @return Uma lista de perfis de usuário.
     */
    public List<PerfilUsuario> getProfiles(long limitResponseLength) {
        List<PerfilUsuario> profiles = new LinkedList<>();
        long countedProfiles = profileRepository.count();

        int[] ints = new Random()
                .ints(1, (int) countedProfiles + 1)
                .distinct()
                .limit(Math.min(limitResponseLength, countedProfiles))
                .toArray();

        for (int i : ints) {
            profiles.add(profileRepository.findById(i).get());
        }

        return profiles;
    }

    /**
     * Obtém uma lista de perfis de usuário com base em nomes específicos.
     *
     * @param firstName O primeiro nome para correspondência.
     * @param lastName  O último nome para correspondência.
     * @return Uma lista de perfis de usuário correspondentes.
     * @throws UsuarioNaoEncontradoException Se nenhum usuário for encontrado.
     */
    public List<PerfilUsuario> getSpecificProfiles(String firstName, String lastName) throws UsuarioNaoEncontradoException {
        Optional<List<Usuario>> optionalUsers;

        if (firstName != null && lastName != null) {
            optionalUsers = userRepository.findAllMatchesByFirstAndLastNames(firstName, lastName);
        } else if (firstName != null) {
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(firstName);
        } else if (lastName != null) {
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(lastName);
        } else {
            return new LinkedList<>();
        }

        return buildProfileList(optionalUsers.orElseThrow(() ->
                new UsuarioNaoEncontradoException("Não foi encontrado o usuário com: " + firstName + " " + lastName + " Nome")));
    }

    /**
     * Constrói uma lista de perfis de usuário a partir de uma lista de usuários.
     *
     * @param users A lista de usuários para os quais perfis devem ser construídos.
     * @return Uma lista de perfis de usuário correspondentes.
     */
    private List<PerfilUsuario> buildProfileList(List<Usuario> users) {
        List<PerfilUsuario> profiles = new LinkedList<>();
        for (Usuario user : users) {
            profiles.add(profileRepository.findByOwner(user).get());
        }
        return profiles;
    }
}
