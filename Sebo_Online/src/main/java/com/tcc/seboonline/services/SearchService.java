package com.tcc.seboonline.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.tcc.seboonline.models.User;
import com.tcc.seboonline.repositories.ProfileRepository;
import com.tcc.seboonline.repositories.UserRepository;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.exceptions.NotUsersFoundException;
import com.tcc.seboonline.models.Profile;


@Service
@AllowSysOut
public class SearchService {


    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserRepository userRepository;


    public List<Profile> getProfiles(long limitResponseLength) {
        List<Profile> profiles = new LinkedList<>();
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



    public List<Profile> getSpecificProfiles(String firstName, String lastName) throws NotUsersFoundException {
        Optional<List<User>> optionalUsers = null;

        if (firstName != null && lastName != null)
            optionalUsers = userRepository.findAllMatchesByFirstAndLastNames(firstName, lastName);
        else if (firstName != null && lastName == null)
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(firstName);
        else if (firstName == null && lastName != null)
            optionalUsers = userRepository.findAllMatchesByEitherFirstOrLastName(lastName);
        else return null;

        List<Profile> profiles = new LinkedList<>();

        if(optionalUsers.isEmpty())
            throw new NotUsersFoundException("Não foi encontrado o usuário com : " + firstName + " " + lastName + " Nome");
        
        for (User user : optionalUsers.get())
            profiles.add(profileRepository.findByOwner(user).get());

        return profiles;
    }
}
