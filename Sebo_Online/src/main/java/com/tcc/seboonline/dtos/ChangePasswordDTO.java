package com.tcc.seboonline.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;
}
