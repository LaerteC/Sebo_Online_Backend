package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EnderecoPerfilDTO {
    private String currentCity;
    private String currentCountry;

    private String bornCity;
    private String bornCountry;

    public EnderecoPerfilDTO(PerfilUsuario profile) {
        this.setCurrentCity(profile.getCurrentCity());
        this.setCurrentCountry(profile.getCurrentCountry());
        this.setBornCity(profile.getBornCity());
        this.setCurrentCountry(profile.getCurrentCountry());
    }
}
