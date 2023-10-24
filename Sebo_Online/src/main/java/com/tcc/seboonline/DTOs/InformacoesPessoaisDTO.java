package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InformacoesPessoaisDTO {
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String phoneNumber;
    private String dob;

    public InformacoesPessoaisDTO(PerfilUsuario profile) {
        this.setFirstName(profile.getOwner().getFirstName());
        this.setLastName(profile.getOwner().getLastName());
        this.setEmail(profile.getOwner().getEmail());
        this.setDob(profile.getDob());
        this.setPhoneNumber(profile.getPhoneNumber());
        this.setGender(profile.getGender());
    }
}
