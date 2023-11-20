package com.tcc.seboonline.modelos;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LivroUsuario {
    @Id
    private String email;
    private String nome_livro;
    private String genero_livro;
    private String first_name;
    private String last_name;

}
