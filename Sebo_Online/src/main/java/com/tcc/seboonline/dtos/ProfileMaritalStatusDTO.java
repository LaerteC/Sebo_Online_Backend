package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileMaritalStatusDTO {
    private String maritalStatus;
    

    public ProfileMaritalStatusDTO(Profile profile) {
        this.setMaritalStatus(profile.getMaritalStatus());
    }
}
