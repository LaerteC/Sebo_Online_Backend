package com.tcc.seboonline.repositorios;

import java.util.List;

import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.LivroUsuario;
import com.tcc.seboonline.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroPostadoRepository extends JpaRepository<LivroPostado, Integer> {
    List<LivroPostado> findAllByAuthor(Usuario user);

    @Query(value = "SELECT * FROM livropostado WHERE genero_livro = :genero_livro", nativeQuery = true)
    List<LivroPostado> findLivroPostadoByGeneroLivro(String genero_livro);

    @Query(value = "SELECT * FROM livropostado WHERE ocultar = false", nativeQuery = true)
    List<LivroPostado> findByOcultar();


}
