package com.tcc.seboonline.DTOs;

import com.tcc.seboonline.modelos.PerfilUsuario;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EstadoCivilDTO {
    private String maritalStatus;
    

    public EstadoCivilDTO(PerfilUsuario profile) {
        this.setMaritalStatus(profile.getMarital_status());
    }
}
