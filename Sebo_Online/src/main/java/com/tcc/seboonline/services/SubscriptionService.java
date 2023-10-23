package com.tcc.seboonline.services;

import java.util.LinkedList;

import com.tcc.seboonline.dtos.SubscriptionDTO;
import com.tcc.seboonline.models.User;
import com.tcc.seboonline.repositories.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.exceptions.NoSuchSubscriberException;
import com.tcc.seboonline.exceptions.ProfileAlreadySubscribedException;
import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.SelfSubscriptionException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.models.Profile;


@Service
public class SubscriptionService {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileRepository profileRepository;
    
    public Profile patchSubscription(SubscriptionDTO subscriptionDTO, User sessionUser) throws ProfileNotFoundException, UserNotFoundException, ProfileAlreadySubscribedException, SelfSubscriptionException {
        Profile profile = profileService.getProfileByUser(sessionUser);

        LinkedList<Integer> subscriptionIds = profile.getSubscriptionIds() != null ? profile.getSubscriptionIds() : new LinkedList<>();

        Integer idToSubscribe = subscriptionDTO.getId();

        if (subscriptionDTO.getId() == profile.getId())
            throw new SelfSubscriptionException("Você não pode se inscrever.");
        
        if (subscriptionIds.contains(idToSubscribe))
            throw new ProfileAlreadySubscribedException("O perfil já inscrito neste perfil.");
        
        subscriptionIds.add(idToSubscribe);
        profile.setSubscriptionIds(subscriptionIds);

        return profileRepository.save(profile);
    }

    public Profile patchUnsubscribe(SubscriptionDTO subscriptionDTO, User sessionUser) throws ProfileNotFoundException, UserNotFoundException, NoSuchSubscriberException {
        Profile profile = profileService.getProfileByUser(sessionUser);

        LinkedList<Integer> subscriptionIds = profile.getSubscriptionIds() != null ? profile.getSubscriptionIds() : new LinkedList<>();

        Integer idToUnsubscribe = subscriptionDTO.getId();

        if (!(subscriptionIds.contains(idToUnsubscribe))) 
                throw new NoSuchSubscriberException("Não é possível cancelar a assinatura de um usuário ao qual você não está inscrito");

        subscriptionIds.remove(idToUnsubscribe);
        profile.setSubscriptionIds(subscriptionIds);


        return profileRepository.save(profile);
    }
}
