package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ProfileLocationDTO {
    private String currentCity;
    private String currentCountry;

    private String bornCity;
    private String bornCountry;

    public ProfileLocationDTO (Profile profile) {
        this.setCurrentCity(profile.getCurrentCity());
        this.setCurrentCountry(profile.getCurrentCountry());
        this.setBornCity(profile.getBornCity());
        this.setCurrentCountry(profile.getCurrentCountry());
    }
}
