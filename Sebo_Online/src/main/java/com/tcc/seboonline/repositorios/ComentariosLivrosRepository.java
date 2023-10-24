package com.tcc.seboonline.repositorios;

import com.tcc.seboonline.modelos.ComentariosLivros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentariosLivrosRepository extends JpaRepository<ComentariosLivros, Integer> {}
