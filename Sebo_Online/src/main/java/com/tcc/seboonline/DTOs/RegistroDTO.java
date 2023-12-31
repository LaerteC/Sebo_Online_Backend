package com.tcc.seboonline.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
