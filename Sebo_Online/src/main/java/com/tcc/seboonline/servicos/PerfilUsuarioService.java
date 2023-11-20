package com.tcc.seboonline.servicos;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.repositorios.PerfilUsuarioRepository;
import com.tcc.seboonline.repositorios.UsuarioRepository;
import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcc.seboonline.DTOs.MudarSenhaDTO;
import com.tcc.seboonline.DTOs.InformacoesPessoaisDTO;
import com.tcc.seboonline.DTOs.ImagemUrlDTO;
import com.tcc.seboonline.DTOs.PerfilAcademicoDTO;
import com.tcc.seboonline.DTOs.EnderecoPerfilDTO;
import com.tcc.seboonline.DTOs.EstadoCivilDTO;
import com.tcc.seboonline.DTOs.ProfissaoDTO;
import com.tcc.seboonline.excecoes.EmailReservadoException;
import com.tcc.seboonline.excecoes.ImagemNãoEncontradaException;
import com.tcc.seboonline.excecoes.SemNomeException;
import com.tcc.seboonline.excecoes.PerfilNaoEncontradoException;
import com.tcc.seboonline.excecoes.UsuarioNEncontradoException;
import com.tcc.seboonline.excecoes.FormatoIdErradoException;
import com.tcc.seboonline.excecoes.SenhaIncorretaException;


@Service
@AllowSysOut
public class PerfilUsuarioService {
    
    @Autowired
    private PerfilUsuarioRepository perfilUsuarioRepository;

    @Autowired
    private UsuarioService userService;

    @Autowired
    private UsuarioRepository userRepository;

    public com.tcc.seboonline.modelos.PerfilUsuario registerProfile(Usuario user) {
        com.tcc.seboonline.modelos.PerfilUsuario profile = new com.tcc.seboonline.modelos.PerfilUsuario();
        profile.setOwner(user);

        return perfilUsuarioRepository.save(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario getProfileByUser(Usuario user) throws PerfilNaoEncontradoException, UsuarioNEncontradoException {
        Optional<Usuario> optionalUser = userService.findByCredentials(user.getEmail(), user.getPassword());

        if (optionalUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário não foi encontrado.");

        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(optionalUser.get());

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado a este usuário  " + user.getFirstName() + " "
                                               + user.getLastName() + " não foi encontrado");

        return optionalProfile.get();
    }

    public com.tcc.seboonline.modelos.PerfilUsuario getProfile(int id) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile  = perfilUsuarioRepository.findById(id);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado a este usuário com ID " + id + " não foi encontrado");
        
        return optionalProfile.get();
    }

    public Usuario changePassword(MudarSenhaDTO changePassword, Usuario sessionUser) throws SenhaIncorretaException, EmailReservadoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), changePassword.getOldPassword());

        if (repoUser.isEmpty())
            throw new SenhaIncorretaException("A senha atual está errada.");

        Usuario user = repoUser.get();
        user.setPassword(changePassword.getNewPassword());

        return userRepository.save(user);
    }

    public InformacoesPessoaisDTO getGeneralInformation(Usuario sessionUser) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Usuário não encontrado.");
        
        com.tcc.seboonline.modelos.PerfilUsuario profile = optionalProfile.get();

        return new InformacoesPessoaisDTO(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario updateGeneralInformation(InformacoesPessoaisDTO generalInfo, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException, SemNomeException, EmailReservadoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        if (generalInfo.getFirstName().trim().equals(""))
            throw new SemNomeException("O primeiro nome não pode estar vazio ou consistir em espaços");
        user.setFirstName(generalInfo.getFirstName());

        if (generalInfo.getLastName().trim().equals(""))
            throw new SemNomeException("O sobrenome não pode estar vazio ou consistir em espaços");
        user.setLastName(generalInfo.getLastName());
        user.setEmail(generalInfo.getEmail());
        
        profile.setOwner(user);
        profile.setGender(generalInfo.getGender());
        profile.setDob(generalInfo.getDob());
        profile.setPhoneNumber(generalInfo.getPhoneNumber());

        if (!sessionUser.getEmail().equals(generalInfo.getEmail()) && userRepository.findByEmail(generalInfo.getEmail()).isPresent())
            throw new EmailReservadoException("The email " + generalInfo.getEmail() + " is being used.");

        userRepository.save(user);
        return perfilUsuarioRepository.save(profile);
    }

    public EnderecoPerfilDTO getProfileLocation(Usuario sessionUser) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + "não foi encontrado.");
        
        com.tcc.seboonline.modelos.PerfilUsuario profile = optionalProfile.get();

        return new EnderecoPerfilDTO(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileLocation(EnderecoPerfilDTO profileLocation, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        profile.setCurrentCity(profileLocation.getCurrentCity());
        profile.setCurrentCountry(profileLocation.getCurrentCountry());
        profile.setBornCity(profileLocation.getBornCity());
        profile.setBornCountry(profileLocation.getBornCountry());

        return perfilUsuarioRepository.save(profile);
    }

    public PerfilAcademicoDTO getProfileEducation(Usuario sessionUser) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Não foi encontrado.");
        
        com.tcc.seboonline.modelos.PerfilUsuario profile = optionalProfile.get();

        return new PerfilAcademicoDTO(profile);
    }


    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileEducation(PerfilAcademicoDTO profileEducation, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        profile.setSchoolName(profileEducation.getSchoolName());

        return perfilUsuarioRepository.save(profile);
    }


    public ProfissaoDTO getProfileWork(Usuario sessionUser) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + "Não foi encontrado.");
        
        com.tcc.seboonline.modelos.PerfilUsuario profile = optionalProfile.get();

        return new ProfissaoDTO(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileWork(ProfissaoDTO profileWork, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        profile.setJobTitle(profileWork.getJobTitle());
        profile.setCompanyName(profileWork.getCompanyName());
        profile.setCompanyUrl(profileWork.getCompanyUrl());

        return perfilUsuarioRepository.save(profile);
    }


    public EstadoCivilDTO getProfileMaritalStatus(Usuario sessionUser) throws PerfilNaoEncontradoException {
        Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findByOwner(sessionUser);

        if (optionalProfile.isEmpty()) 
            throw new PerfilNaoEncontradoException("O perfil relacionado para " + sessionUser.getFirstName() + " "
                        + sessionUser.getLastName() + " Não foi encontrado.");
        
        com.tcc.seboonline.modelos.PerfilUsuario profile = optionalProfile.get();

        return new EstadoCivilDTO(profile);
    }


    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileMaritalStatus(EstadoCivilDTO profileMaritalStatus, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        profile.setMarital_status(profileMaritalStatus.getMaritalStatus());

        return perfilUsuarioRepository.save(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileBackground(ImagemUrlDTO profileBackground, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        profile.setBackgroundImageUrl(profileBackground.getUrl());

        return perfilUsuarioRepository.save(profile);
    }

    public com.tcc.seboonline.modelos.PerfilUsuario updateProfileAvatar(ImagemUrlDTO avatar, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        user.setAvatarImageUrl(avatar.getUrl());

        profile.setOwner(user);
        userRepository.save(user);

        return perfilUsuarioRepository.save(profile);
    }

    public List<com.tcc.seboonline.modelos.PerfilUsuario> getAllProfilesByIds(String commaSeparatedIds) throws FormatoIdErradoException {
        return getAllProfilesByIds(commaSeparatedIds, -1, false);
    }
    public List<com.tcc.seboonline.modelos.PerfilUsuario> getAllProfilesByIds(String commaSeparatedIds, long limit) throws FormatoIdErradoException {
        return getAllProfilesByIds(commaSeparatedIds, limit, false);
    }
    public List<com.tcc.seboonline.modelos.PerfilUsuario> getAllProfilesByIds(String commaSeparatedIds, long limit, boolean shuffle) throws FormatoIdErradoException {
        List<Integer> ids;
        List<com.tcc.seboonline.modelos.PerfilUsuario> profiles = new LinkedList<>();

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
            throw new FormatoIdErradoException("O endpoint aceita apenas números separados por vírgula. Sua solicitação é: " + commaSeparatedIds);
        }

        if (shuffle) Collections.shuffle(ids);

        for (int id : ids) {
            Optional<com.tcc.seboonline.modelos.PerfilUsuario> optionalProfile = perfilUsuarioRepository.findById(id);

            if (optionalProfile.isPresent())
                profiles.add(optionalProfile.get());
        }

        return profiles;
    }


    public com.tcc.seboonline.modelos.PerfilUsuario updatePhotos(ImagemUrlDTO imageUrlDTO, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        ArrayDeque<String> photoUrls = profile.getPhotoUrls() != null ? profile.getPhotoUrls() : new ArrayDeque<>();

        photoUrls.addFirst(imageUrlDTO.getUrl());
        profile.setPhotoUrls(photoUrls);

        return perfilUsuarioRepository.save(profile);
    }


    public com.tcc.seboonline.modelos.PerfilUsuario removePhoto(ImagemUrlDTO imageUrlDTO, Usuario sessionUser) throws UsuarioNEncontradoException, PerfilNaoEncontradoException, ImagemNãoEncontradaException {
        Optional<Usuario> repoUser = userService.findByCredentials(sessionUser.getEmail(), sessionUser.getPassword());

        if (repoUser.isEmpty())
            throw new UsuarioNEncontradoException("O usuário da sessão não foi encontrado. Tente fazer login novamente");

        Usuario user = repoUser.get();

        com.tcc.seboonline.modelos.PerfilUsuario profile = getProfileByUser(user);

        ArrayDeque<String> photoUrls = profile.getPhotoUrls();

        if (photoUrls == null)
            throw new ImagemNãoEncontradaException("A foto não foi encontrada");

        photoUrls.remove(imageUrlDTO.getUrl());
        profile.setPhotoUrls(photoUrls);

        return perfilUsuarioRepository.save(profile);
    }

    
}
