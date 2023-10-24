package com.tcc.seboonline.repositorios;

import com.tcc.seboonline.modelos.LivroPostado;
import com.tcc.seboonline.modelos.Usuario;
import com.tcc.seboonline.modelos.Favoritos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FavoritoRepository extends JpaRepository<Favoritos, Integer> {
    Optional<Favoritos> findTopByPostAndUserOrderByVoteIdDesc(LivroPostado post, Usuario currentUser);

    long deleteByPost(LivroPostado post);
}
