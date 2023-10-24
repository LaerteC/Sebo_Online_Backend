package com.tcc.seboonline.servicos;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.UsuarioNaoEncontradoException;
import com.tcc.seboonline.modelos.PerfilUsuario;


@Service
@AllowSysOut
public class PesquisarLivroService {


    @Autowired
    private PerfilUsuarioRepository profileRepository;

    @Autowired
    private UsuarioRepository userRepository;


    public List<PerfilUsuario> getProfiles(long limitResponseLength) {
        List<PerfilUsuario> profiles = new LinkedList<>();
        long countedProfiles = profileRepository.count();

        int[] ints = new Random()
                                .ints(1, ((int) countedProfiles) + 1 )
                                .distinct()
                                .limit(limitResponseLength > countedProfiles ? countedProfiles : limitResponseLength)
                                .toArray();
   
        for (int i : ints) 
            profiles.add(profileRepository.findById(i).get());

        return profiles;
    }



    public List<PerfilUsuario> getSpecificProfiles(String firstName, String lastName) throws UsuarioNaoEncontradoException {
        Optional<List<Usuario>> optionalUsers = null;

        if (firstName != null && lastName != null)
            optionalUsers = userRepository.findAllMatchesByFirstAndLastNames(firstName, lastName);
        else if (firstName != null && lastName == null)
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(firstName);
        else if (firstName == null && lastName != null)
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(lastName);
        else return null;

        List<PerfilUsuario> profiles = new LinkedList<>();

        if(optionalUsers.isEmpty())
            throw new UsuarioNaoEncontradoException("Não foi encontrado o usuário com : " + firstName + " " + lastName + " Nome");
        
        for (Usuario user : optionalUsers.get())
            profiles.add(profileRepository.findByOwner(user).get());

        return profiles;
    }
}
