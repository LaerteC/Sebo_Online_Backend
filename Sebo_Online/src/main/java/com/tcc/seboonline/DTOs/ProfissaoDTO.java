package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ProfissaoDTO {
    private String jobTitle;

    private String companyName;
    private String companyUrl;

    public ProfissaoDTO(PerfilUsuario profile) {
        this.setJobTitle(profile.getJobTitle());
        this.setCompanyName(profile.getCompanyName());
        this.setCompanyUrl(profile.getCompanyUrl());
    }

}
