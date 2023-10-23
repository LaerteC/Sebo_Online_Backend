package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class ProfileEducationDTO {
    private String schoolName;

    public ProfileEducationDTO(Profile profile) {
        this.setSchoolName(profile.getSchoolName());
    }

}
