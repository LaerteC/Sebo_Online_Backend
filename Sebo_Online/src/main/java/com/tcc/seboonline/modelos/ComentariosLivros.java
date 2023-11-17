package com.tcc.seboonline.modelos;

import java.io.Serializable;
import java.util.List;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ComentariosLivros")
public class ComentariosLivros implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	private String text;
	private String imageUrl;

    @OneToMany(cascade = CascadeType.ALL)
	private List<ComentariosLivros> comentario;

    @ManyToOne
	private Usuario author;
}
