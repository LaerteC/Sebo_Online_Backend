package com.tcc.seboonline.repositorios;

import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.LivroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroUsuarioRepository extends JpaRepository<LivroUsuario, String> {



    @Query(value = "SELECT email, nome_livro, genero_livro, first_name, last_name FROM livropostado AS livro " +
            "INNER JOIN usuario AS usu ON livro.author_id = usu.id " +
            "WHERE livro.id = :id", nativeQuery = true)
    LivroUsuario findEmailUser(@Param("id") int id);

}
