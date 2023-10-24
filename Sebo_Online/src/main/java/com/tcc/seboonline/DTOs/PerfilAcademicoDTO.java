package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PerfilAcademicoDTO {
    private String schoolName;

    public PerfilAcademicoDTO(PerfilUsuario profile) {
        this.setSchoolName(profile.getSchoolName());
    }

}
