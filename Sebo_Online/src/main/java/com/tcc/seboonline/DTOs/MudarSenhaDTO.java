package com.tcc.seboonline.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MudarSenhaDTO {
    private String oldPassword;
    private String newPassword;
}
