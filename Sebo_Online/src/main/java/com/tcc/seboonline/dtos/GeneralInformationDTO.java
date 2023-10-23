package com.tcc.seboonline.dtos;

import com.tcc.seboonline.models.Profile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GeneralInformationDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String dob;

    public GeneralInformationDTO (Profile profile) {
        this.setFirstName(profile.getOwner().getFirstName());
        this.setLastName(profile.getOwner().getLastName());
        this.setEmail(profile.getOwner().getEmail());
        this.setDob(profile.getDob());
        this.setPhoneNumber(profile.getPhoneNumber());
        this.setGender(profile.getGender());
    }
}
