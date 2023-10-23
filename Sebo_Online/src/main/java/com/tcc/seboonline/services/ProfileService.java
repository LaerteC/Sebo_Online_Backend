package com.tcc.seboonline.services;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tcc.seboonline.models.User;
import com.tcc.seboonline.repositories.ProfileRepository;
import com.tcc.seboonline.repositories.UserRepository;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.dtos.ChangePasswordDTO;
import com.tcc.seboonline.dtos.GeneralInformationDTO;
import com.tcc.seboonline.dtos.ImageUrlDTO;
import com.tcc.seboonline.dtos.ProfileEducationDTO;
import com.tcc.seboonline.dtos.ProfileLocationDTO;
import com.tcc.seboonline.dtos.ProfileMaritalStatusDTO;
import com.tcc.seboonline.dtos.ProfileWorkDTO;
import com.tcc.seboonline.exceptions.EmailReservedException;
import com.tcc.seboonline.exceptions.ImageNotFoundException;
import com.tcc.seboonline.exceptions.NoNameException;
import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.exceptions.WrongIdsFormatException;
import com.tcc.seboonline.exceptions.WrongPasswordException;
import com.tcc.seboonline.models.Profile;



@Service
@AllowSysOut
public class ProfileService {
    
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    public Profile registerProfile(User user) {
        Profile profile = new Profile();
        profile.setOwner(user);

        return profileRepository.save(profile);
    }

    public Profile getProfileByUser(User user) throws ProfileNotFoundException, UserNotFoundException {
        Optional<User> optionalUser = userService.findByCredentials(user.getEmail(), user.getPassword());

        if (optionalUser.isEmpty())
            throw new UserNotFoundException("O usuário não foi encontrado.");

        Optional<Profile> optionalProfile = profileRepository.findByOwner(optionalUser.get());

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado a este usuário  " + user.getFirstName() + " "
                                               + user.getLastName() + " não foi encontrado");

        return optionalProfile.get();
    }

    public Profile getProfile(int id) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile  = profileRepository.findById(id);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException ("O perfil relacionado a este usuário com ID " + id + " não foi encontrado");
        
        return optionalProfile.get();
    }

    public User changePassword(ChangePasswordDTO changePassword, User sessionUser) throws WrongPasswordException, EmailReservedException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), changePassword.getOldPassword());

        if (repoUser.isEmpty())
            throw new WrongPasswordException("A senha atual está errada.");

        User user = repoUser.get();
        user.setPassword(changePassword.getNewPassword());

        return userRepository.save(user);
    }

    public GeneralInformationDTO getGeneralInformation(User sessionUser) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Usuário não encontrado.");
        
        Profile profile = optionalProfile.get();

        return new GeneralInformationDTO(profile);
    }

    public Profile updateGeneralInformation(GeneralInformationDTO generalInfo, User sessionUser) throws UserNotFoundException, ProfileNotFoundException, NoNameException, EmailReservedException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        if (generalInfo.getFirstName().trim().equals(""))
            throw new NoNameException("O primeiro nome não pode estar vazio ou consistir em espaços");
        user.setFirstName(generalInfo.getFirstName());

        if (generalInfo.getLastName().trim().equals(""))
            throw new NoNameException("O sobrenome não pode estar vazio ou consistir em espaços");
        user.setLastName(generalInfo.getLastName());
        user.setEmail(generalInfo.getEmail());
        
        profile.setOwner(user);
        profile.setGender(generalInfo.getGender());
        profile.setDob(generalInfo.getDob());
        profile.setPhoneNumber(generalInfo.getPhoneNumber());

        if (!sessionUser.getEmail().equals(generalInfo.getEmail()) && userRepository.findByEmail(generalInfo.getEmail()).isPresent())
            throw new EmailReservedException("The email " + generalInfo.getEmail() + " is being used.");

        userRepository.save(user);
        return profileRepository.save(profile);
    }

    public ProfileLocationDTO getProfileLocation(User sessionUser) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + "não foi encontrado.");
        
        Profile profile = optionalProfile.get();

        return new ProfileLocationDTO(profile);
    }

    public Profile updateProfileLocation(ProfileLocationDTO profileLocation, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        profile.setCurrentCity(profileLocation.getCurrentCity());
        profile.setCurrentCountry(profileLocation.getCurrentCountry());
        profile.setBornCity(profileLocation.getBornCity());
        profile.setBornCountry(profileLocation.getBornCountry());

        return profileRepository.save(profile);
    }

    public ProfileEducationDTO getProfileEducation(User sessionUser) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Não foi encontrado.");
        
        Profile profile = optionalProfile.get();

        return new ProfileEducationDTO(profile);
    }


    public Profile updateProfileEducation(ProfileEducationDTO profileEducation, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        profile.setSchoolName(profileEducation.getSchoolName());

        return profileRepository.save(profile);
    }


    public ProfileWorkDTO getProfileWork(User sessionUser) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + "Não foi encontrado.");
        
        Profile profile = optionalProfile.get();

        return new ProfileWorkDTO(profile);
    }

    public Profile updateProfileWork(ProfileWorkDTO profileWork, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        profile.setJobTitle(profileWork.getJobTitle());
        profile.setCompanyName(profileWork.getCompanyName());
        profile.setCompanyUrl(profileWork.getCompanyUrl());

        return profileRepository.save(profile);
    }


    public ProfileMaritalStatusDTO getProfileMaritalStatus(User sessionUser) throws ProfileNotFoundException {
        Optional<Profile> optionalProfile = profileRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new ProfileNotFoundException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Não foi encontrado.");
        
        Profile profile = optionalProfile.get();

        return new ProfileMaritalStatusDTO(profile);
    }


    public Profile updateProfileMaritalStatus(ProfileMaritalStatusDTO profileMaritalStatus, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        profile.setMaritalStatus(profileMaritalStatus.getMaritalStatus());

        return profileRepository.save(profile);
    }

    public Profile updateProfileBackground(ImageUrlDTO profileBackground, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        profile.setBackgroundImageUrl(profileBackground.getUrl());

        return profileRepository.save(profile);
    }

    public Profile updateProfileAvatar(ImageUrlDTO avatar, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        user.setAvatarImageUrl(avatar.getUrl());

        profile.setOwner(user);
        userRepository.save(user);

        return profileRepository.save(profile);
    }

    public List<Profile> getAllProfilesByIds(String commaSeparatedIds) throws WrongIdsFormatException {
        return getAllProfilesByIds(commaSeparatedIds, -1, false);
    }
    public List<Profile> getAllProfilesByIds(String commaSeparatedIds, long limit) throws WrongIdsFormatException {
        return getAllProfilesByIds(commaSeparatedIds, limit, false);
    }
    public List<Profile> getAllProfilesByIds(String commaSeparatedIds, long limit, boolean shuffle) throws WrongIdsFormatException {
        List<Integer> ids;
        List<Profile> profiles = new LinkedList<>();


        try { 
            if (limit > 0)  {
                ids = Arrays.stream(commaSeparatedIds.split(","))
                            .mapToInt(Integer::parseInt)
                            .limit(limit)
                            .filter(x -> x > 0)
                            .distinct()
                            .boxed()
                            .collect(Collectors.toList());
            } else {
                ids = Arrays.stream(commaSeparatedIds.split(","))
                            .mapToInt(Integer::parseInt)
                            .filter(x -> x > 0)
                            .distinct()
                            .boxed()
                            .collect(Collectors.toList());
            }
        } 
        catch (NumberFormatException e) {
            throw new WrongIdsFormatException("O endpoint aceita apenas números separados por vírgula. Sua solicitação é: " + commaSeparatedIds);
        }

        if (shuffle) Collections.shuffle(ids);

        for (int id : ids) {
            Optional<Profile> optionalProfile = profileRepository.findById(id);

            if (optionalProfile.isPresent())
                profiles.add(optionalProfile.get());
        }


        return profiles;
    }


    public Profile updatePhotos(ImageUrlDTO imageUrlDTO, User sessionUser) throws UserNotFoundException, ProfileNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        ArrayDeque<String> photoUrls = profile.getPhotoUrls() != null ? profile.getPhotoUrls() : new ArrayDeque<>();

        photoUrls.addFirst(imageUrlDTO.getUrl());
        profile.setPhotoUrls(photoUrls);

        return profileRepository.save(profile);
    }


    public Profile removePhoto(ImageUrlDTO imageUrlDTO, User sessionUser) throws UserNotFoundException, ProfileNotFoundException, ImageNotFoundException {
        Optional<User> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UserNotFoundException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        User user = repoUser.get();

        Profile profile = getProfileByUser(user);

        ArrayDeque<String> photoUrls = profile.getPhotoUrls();

        if (photoUrls == null)
            throw new ImageNotFoundException("A foto não foi encontrada");

        photoUrls.remove(imageUrlDTO.getUrl());
        profile.setPhotoUrls(photoUrls);

        return profileRepository.save(profile);
    }

    
}
