package com.tcc.seboonline.controllers;

import com.tcc.seboonline.annotations.Authorized;
import com.tcc.seboonline.dtos.ChangePasswordDTO;
import com.tcc.seboonline.dtos.GeneralInformationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tcc.seboonline.exceptions.EmailReservedException;
import com.tcc.seboonline.exceptions.ImageNotFoundException;
import com.tcc.seboonline.exceptions.NoNameException;
import com.tcc.seboonline.dtos.ImageUrlDTO;
import com.tcc.seboonline.dtos.ProfileEducationDTO;
import com.tcc.seboonline.dtos.ProfileLocationDTO;
import com.tcc.seboonline.dtos.ProfileMaritalStatusDTO;
import com.tcc.seboonline.dtos.ProfileWorkDTO;
import com.tcc.seboonline.exceptions.ProfileNotFoundException;
import com.tcc.seboonline.exceptions.UserNotFoundException;
import com.tcc.seboonline.exceptions.WrongIdsFormatException;
import com.tcc.seboonline.exceptions.WrongPasswordException;
import com.tcc.seboonline.models.Message;
import com.tcc.seboonline.models.Profile;
import com.tcc.seboonline.models.User;

import java.util.List;


import jakarta.servlet.http.HttpSession;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.seboonline.services.ProfileService;

@RestController
@RequestMapping("/profile")
@AllowSysOut
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ProfileService profileService;

    @Authorized
    @GetMapping
    public ResponseEntity<Object> getOwnProfile(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.getProfileByUser(user);
            return ResponseEntity.ok(profile);
        } catch (UserNotFoundException e) {
            logger.error("Usuário não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.info("Perfil não encontrado, registrando perfil para o usuário: " + user.getId());
            return ResponseEntity.ok(profileService.registerProfile(user));
        }
    }

    @Authorized
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable int id) {
        try {
            Profile profile = profileService.getProfile(id);
            return ResponseEntity.ok(profile);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            user = profileService.changePassword(changePasswordDTO, user);
            session.setAttribute("user", user);
            return ResponseEntity.ok(new Message<>("A senha foi atualizada com sucesso", user));
        } catch (WrongPasswordException e) {
            logger.error("Erro ao alterar a senha: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EmailReservedException e) {
            logger.error("Erro ao alterar a senha: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/general-information")
    public ResponseEntity<Object> getGeneralInformation(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            GeneralInformationDTO generalInformation = profileService.getGeneralInformation(user);
            return ResponseEntity.ok(generalInformation);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/general-information")
    public ResponseEntity<Object> updateGeneralInformation(@RequestBody GeneralInformationDTO generalInfo, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateGeneralInformation(generalInfo, user);
            session.setAttribute("user", profile.getOwner());
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar informações gerais do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoNameException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EmailReservedException e) {
            logger.error("Erro ao atualizar informações gerais do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/profile-location")
    public ResponseEntity<Object> getProfileLocation(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            ProfileLocationDTO profileLocation = profileService.getProfileLocation(user);
            return ResponseEntity.ok(profileLocation);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-location")
    public ResponseEntity<Object> updateProfileLocation(@RequestBody ProfileLocationDTO profileLocation, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateProfileLocation(profileLocation, user);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar a localização do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar a localização do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/profile-education")
    public ResponseEntity<Object> getProfileEducation(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            ProfileEducationDTO profileEducation = profileService.getProfileEducation(user);
            return ResponseEntity.ok(profileEducation);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-education")
    public ResponseEntity<Object> updateProfileEducation(@RequestBody ProfileEducationDTO profileEducation, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateProfileEducation(profileEducation, user);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar a educação do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar a educação do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/profile-work")
    public ResponseEntity<Object> getProfileWork(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            ProfileWorkDTO profileWork = profileService.getProfileWork(user);
            return ResponseEntity.ok(profileWork);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-work")
    public ResponseEntity<Object> updateProfileWork(@RequestBody ProfileWorkDTO profileWork, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateProfileWork(profileWork, user);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar o trabalho do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar o trabalho do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/profile-marital-status")
    public ResponseEntity<Object> getProfileMaritalStatus(HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            ProfileMaritalStatusDTO profileMaritalStatus = profileService.getProfileMaritalStatus(user);
            return ResponseEntity.ok(profileMaritalStatus);
        } catch (ProfileNotFoundException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-marital-status")
    public ResponseEntity<Object> updateProfileMaritalStatus(@RequestBody ProfileMaritalStatusDTO profileMaritalStatus, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateProfileMaritalStatus(profileMaritalStatus, user);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar o estado civil do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar o estado civil do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-background")
    public ResponseEntity<Object> updateProfileBackground(@RequestBody ImageUrlDTO profileBackground, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            logger.info("Atualizando fundo do perfil: " + profileBackground);
            Profile profile = profileService.updateProfileBackground(profileBackground, user);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar o fundo do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar o fundo do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PostMapping("/profile-avatar")
    public ResponseEntity<Object> updateProfileAvatar(@RequestBody ImageUrlDTO avatar, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            Profile profile = profileService.updateProfileAvatar(avatar, user);
            User updatedUser = profile.getOwner();
            session.setAttribute("user", updatedUser);
            return ResponseEntity.ok(new Message<Profile>("O perfil foi atualizado com sucesso.", profile));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar o avatar do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar o avatar do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProfilesByIds(@RequestParam(name = "ids") String ids,
                                                      @RequestParam(name = "limit", required = false) String limit,
                                                      @RequestParam(name = "shuffle", required = false) String shuffle)
    {
        long parsedLimit;
        boolean parsedShuffle;
        List<Profile> profiles;

        try {
            if (limit != null && shuffle != null) {
                parsedLimit = Long.parseLong(limit);
                parsedShuffle = Boolean.parseBoolean(shuffle);
                profiles = profileService.getAllProfilesByIds(ids, parsedLimit, parsedShuffle);
            } else if (limit != null && shuffle == null) {
                parsedLimit = Long.parseLong(limit);
                profiles = profileService.getAllProfilesByIds(ids, parsedLimit);
            } else
                profiles = profileService.getAllProfilesByIds(ids);

            return ResponseEntity.ok(profiles);
        } catch (WrongIdsFormatException e) {
            logger.error("Formato de IDs incorreto: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Formato de consulta incorreto. O parâmetro limite deve ser um número.", e);
            return ResponseEntity.badRequest().body(new Message<>("Formato de consulta incorreto. O parâmetro limite deve ser um número.", null));
        }
    }

    @Authorized
    @PutMapping("/update-photos")
    public ResponseEntity<Object> updatePhotos(@RequestBody ImageUrlDTO imageUrlDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            return ResponseEntity.ok(profileService.updatePhotos(imageUrlDTO, user));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao atualizar fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao atualizar fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Authorized
    @PatchMapping("/update-photos")
    public ResponseEntity<Object> removePhoto(@RequestBody ImageUrlDTO imageUrlDTO, HttpSession session) {
        User user = (User) session.getAttribute("user");

        try {
            return ResponseEntity.ok(profileService.removePhoto(imageUrlDTO, user));
        } catch (UserNotFoundException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ProfileNotFoundException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ImageNotFoundException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Outros métodos mantidos com a mesma estrutura
    // ...
}

