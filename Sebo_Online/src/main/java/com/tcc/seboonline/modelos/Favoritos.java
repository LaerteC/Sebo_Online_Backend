package com.tcc.seboonline.modelos;

import jakarta.persistence.*;
import lombok.*;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "favoritos")
public class Favoritos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer voteId;
    private EnumTipoFavorito voteType;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private LivroPostado post;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario user;

}
