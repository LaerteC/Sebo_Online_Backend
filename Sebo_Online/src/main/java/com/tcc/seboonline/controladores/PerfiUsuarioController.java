package com.tcc.seboonline.controladores;

import com.tcc.seboonline.annotations.AutorizacaoUsuario;
import com.tcc.seboonline.DTOs.MudarSenhaDTO;
import com.tcc.seboonline.DTOs.InformacoesPessoaisDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.tcc.seboonline.excecoes.EmailReservadoException;
import com.tcc.seboonline.excecoes.ImagemNãoEncontradaException;
import com.tcc.seboonline.excecoes.SemNomeException;
import com.tcc.seboonline.DTOs.ImagemUrlDTO;
import com.tcc.seboonline.DTOs.PerfilAcademicoDTO;
import com.tcc.seboonline.DTOs.EnderecoPerfilDTO;
import com.tcc.seboonline.DTOs.EstadoCivilDTO;
import com.tcc.seboonline.DTOs.ProfissaoDTO;
import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.excecoes.FormatoIdErradoException;
import com.tcc.seboonline.excecoes.SenhaIncorretaException;
import com.tcc.seboonline.modelos.MensagensGenericas;
import com.tcc.seboonline.modelos.Usuario;

import java.util.List;


import jakarta.servlet.http.HttpSession;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.seboonline.servicos.PerfilUsuario;

@RestController
@RequestMapping("/profile")
@AllowSysOut
public class PerfiUsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(PerfiUsuarioController.class);

    @Autowired
    private PerfilUsuario profileService;

    @AutorizacaoUsuario
    @GetMapping
    public ResponseEntity<Object> getOwnProfile(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.getProfileByUser(user);
            return ResponseEntity.ok(profile);
        } catch (UsuarioNEncontradoException e) {
            logger.error("Usuário não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.info("Perfil não encontrado, registrando perfil para o usuário: " + user.getId());
            return ResponseEntity.ok(profileService.registerProfile(user));
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/{id}")
    public ResponseEntity<Object> getProfile(@PathVariable int id) {
        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.getProfile(id);
            return ResponseEntity.ok(profile);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody MudarSenhaDTO changePasswordDTO, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            user = profileService.changePassword(changePasswordDTO, user);
            session.setAttribute("user", user);
            return ResponseEntity.ok(new MensagensGenericas<>("A senha foi atualizada com sucesso", user));
        } catch (SenhaIncorretaException e) {
            logger.error("Erro ao alterar a senha: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EmailReservadoException e) {
            logger.error("Erro ao alterar a senha: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/general-information")
    public ResponseEntity<Object> getGeneralInformation(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            InformacoesPessoaisDTO generalInformation = profileService.getGeneralInformation(user);
            return ResponseEntity.ok(generalInformation);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/general-information")
    public ResponseEntity<Object> updateGeneralInformation(@RequestBody InformacoesPessoaisDTO generalInfo, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateGeneralInformation(generalInfo, user);
            session.setAttribute("user", profile.getOwner());
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar informações gerais do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SemNomeException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EmailReservadoException e) {
            logger.error("Erro ao atualizar informações gerais do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/profile-location")
    public ResponseEntity<Object> getProfileLocation(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            EnderecoPerfilDTO profileLocation = profileService.getProfileLocation(user);
            return ResponseEntity.ok(profileLocation);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-location")
    public ResponseEntity<Object> updateProfileLocation(@RequestBody EnderecoPerfilDTO profileLocation, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileLocation(profileLocation, user);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar a localização do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar a localização do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/profile-education")
    public ResponseEntity<Object> getProfileEducation(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            PerfilAcademicoDTO profileEducation = profileService.getProfileEducation(user);
            return ResponseEntity.ok(profileEducation);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-education")
    public ResponseEntity<Object> updateProfileEducation(@RequestBody PerfilAcademicoDTO profileEducation, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileEducation(profileEducation, user);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar a educação do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar a educação do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/profile-work")
    public ResponseEntity<Object> getProfileWork(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            ProfissaoDTO profileWork = profileService.getProfileWork(user);
            return ResponseEntity.ok(profileWork);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-work")
    public ResponseEntity<Object> updateProfileWork(@RequestBody ProfissaoDTO profileWork, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileWork(profileWork, user);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar o trabalho do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar o trabalho do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/profile-marital-status")
    public ResponseEntity<Object> getProfileMaritalStatus(HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            EstadoCivilDTO profileMaritalStatus = profileService.getProfileMaritalStatus(user);
            return ResponseEntity.ok(profileMaritalStatus);
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Perfil não encontrado: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-marital-status")
    public ResponseEntity<Object> updateProfileMaritalStatus(@RequestBody EstadoCivilDTO profileMaritalStatus, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileMaritalStatus(profileMaritalStatus, user);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar o estado civil do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar o estado civil do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-background")
    public ResponseEntity<Object> updateProfileBackground(@RequestBody ImagemUrlDTO profileBackground, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            logger.info("Atualizando fundo do perfil: " + profileBackground);
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileBackground(profileBackground, user);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar o fundo do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar o fundo do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PostMapping("/profile-avatar")
    public ResponseEntity<Object> updateProfileAvatar(@RequestBody ImagemUrlDTO avatar, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            com.tcc.seboonline.modelos.PerfilUsuario profile = profileService.updateProfileAvatar(avatar, user);
            Usuario updatedUser = profile.getOwner();
            session.setAttribute("user", updatedUser);
            return ResponseEntity.ok(new MensagensGenericas<com.tcc.seboonline.modelos.PerfilUsuario>("O perfil foi atualizado com sucesso.", profile));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar o avatar do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar o avatar do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @GetMapping("/all")
    public ResponseEntity<Object> getAllProfilesByIds(@RequestParam(name = "ids") String ids,
                                                      @RequestParam(name = "limit", required = false) String limit,
                                                      @RequestParam(name = "shuffle", required = false) String shuffle)
    {
        long parsedLimit;
        boolean parsedShuffle;
        List<com.tcc.seboonline.modelos.PerfilUsuario> profiles;

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
        } catch (FormatoIdErradoException e) {
            logger.error("Formato de IDs incorreto: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Formato de consulta incorreto. O parâmetro limite deve ser um número.", e);
            return ResponseEntity.badRequest().body(new MensagensGenericas<>("Formato de consulta incorreto. O parâmetro limite deve ser um número.", null));
        }
    }

    @AutorizacaoUsuario
    @PutMapping("/update-photos")
    public ResponseEntity<Object> updatePhotos(@RequestBody ImagemUrlDTO imageUrlDTO, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            return ResponseEntity.ok(profileService.updatePhotos(imageUrlDTO, user));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao atualizar fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao atualizar fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @AutorizacaoUsuario
    @PatchMapping("/update-photos")
    public ResponseEntity<Object> removePhoto(@RequestBody ImagemUrlDTO imageUrlDTO, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("user");

        try {
            return ResponseEntity.ok(profileService.removePhoto(imageUrlDTO, user));
        } catch (UsuarioNEncontradoException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (PerfilNaoEncontradoException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (ImagemNãoEncontradaException e) {
            logger.error("Erro ao remover fotos do perfil: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Outros métodos mantidos com a mesma estrutura
    // ...
}

