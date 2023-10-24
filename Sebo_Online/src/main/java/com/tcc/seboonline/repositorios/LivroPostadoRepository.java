package com.tcc.seboonline.repositorios;

import java.util.List;

import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface LivroPostadoRepository extends JpaRepository<LivroPostado, Integer> {
    List<LivroPostado> findAllByAuthor(Usuario user);
}
