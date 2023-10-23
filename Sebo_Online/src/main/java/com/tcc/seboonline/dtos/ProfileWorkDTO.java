package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProfileWorkDTO {
    private String jobTitle;

    private String companyName;
    private String companyUrl;

    public ProfileWorkDTO(Profile profile) {
        this.setJobTitle(profile.getJobTitle());
        this.setCompanyName(profile.getCompanyName());
        this.setCompanyUrl(profile.getCompanyUrl());
    }

}
