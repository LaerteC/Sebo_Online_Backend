package com.tcc.seboonline.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.hibernate.internal.build.AllowSysOut;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
@AllowSysOut
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String avatarImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS3URjWpcZfPfzAHxrU_Xms2GzfUJmvWXGjuw&usqp=CAU";


    public Usuario(int id, String email, String password, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
