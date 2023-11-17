package com.tcc.seboonline.modelos;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.LinkedList;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "perfilusuario")
public class PerfilUsuario implements Serializable{



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String backgroundImageUrl = "https://images.unsplash.com/photo-1501594907352-04cda38ebc29?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1932&q=80";

    private String currentCity;
    private String currentCountry;

    private String bornCity;
    private String bornCountry;

    private String dob;
    private String gender;
    private String maritalStatus;

    private String schoolName;

    private String jobTitle;

    private String companyName;
    private String companyUrl;

    private String phoneNumber;

    /**
     * Represents IDs of other users to which this user is subscribed.
     */
    @Column(length = 4_000)
    private LinkedList<Integer> subscriptionIds = new LinkedList<>();

    @Column(length = 4_000)
    private ArrayDeque<String> photoUrls = new ArrayDeque<>();


    @OneToOne
	private Usuario owner;

    public PerfilUsuario(int id, String backgroundImageUrl, String currentCity, String currentCountry, String bornCity,
                         String bornCountry, String dob, String gender, String maritalStatus, String schoolName, String jobTitle,
                         String companyName, String companyUrl, String phoneNumber, Usuario owner) {
        this.id = id;
        this.backgroundImageUrl = backgroundImageUrl;
        this.currentCity = currentCity;
        this.currentCountry = currentCountry;
        this.bornCity = bornCity;
        this.bornCountry = bornCountry;
        this.dob = dob;
        this.gender = gender;
        this.maritalStatus = maritalStatus;
        this.schoolName = schoolName;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.companyUrl = companyUrl;
        this.phoneNumber = phoneNumber;
        this.owner = owner;
    }
}
