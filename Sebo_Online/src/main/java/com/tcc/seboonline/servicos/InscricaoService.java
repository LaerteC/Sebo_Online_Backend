package com.tcc.seboonline.servicos;

import java.util.LinkedList;

import com.tcc.seboonline.DTOs.AmigosSeguirDTO;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.excecoes.NemAmigoException;
import com.tcc.seboonline.excecoes.AmigoSeguidoException;
import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.AutoInscricaoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;


@Service
public class InscricaoService {

    @Autowired
    private PerfilUsuarioService profileService;

    @Autowired
    private PerfilUsuarioRepository profileRepository;
    
    public com.tcc.seboonline.modelos.PerfilUsuario patchSubscription(AmigosSeguirDTO subscriptionDTO, Usuario sessionUser) throws PerfilNaoEncontradoException, UsuarioNEncontradoException, AmigoSeguidoException, AutoInscricaoException {
        com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.getProfileByUser(sessionUser);

        LinkedList<Integer> subscriptionIds = profile.getSubscriptionIds() != null ? profile.getSubscriptionIds() : new LinkedList<>();

        Integer idToSubscribe = subscriptionDTO.getId();

        if (subscriptionDTO.getId() == profile.getId())
            throw new AutoInscricaoException("Você não pode se inscrever.");
        
        if (subscriptionIds.contains(idToSubscribe))
            throw new AmigoSeguidoException("O perfil já inscrito neste perfil.");
        
        subscriptionIds.add(idToSubscribe);
        profile.setSubscriptionIds(subscriptionIds);

        return profileRepository.save(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario patchUnsubscribe(AmigosSeguirDTO subscriptionDTO, Usuario sessionUser) throws PerfilNaoEncontradoException, UsuarioNEncontradoException, NemAmigoException {
        com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.getProfileByUser(sessionUser);

        LinkedList<Integer> subscriptionIds = profile.getSubscriptionIds() != null ? profile.getSubscriptionIds() : new LinkedList<>();

        Integer idToUnsubscribe = subscriptionDTO.getId();

        if (!(subscriptionIds.contains(idToUnsubscribe))) 
                throw new NemAmigoException("Não é possível cancelar a assinatura de um usuário ao qual você não está inscrito");

        subscriptionIds.remove(idToUnsubscribe);
        profile.setSubscriptionIds(subscriptionIds);


        return profileRepository.save(profile);
    }
}
