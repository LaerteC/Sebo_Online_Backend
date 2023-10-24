package com.tcc.seboonline.repositorios;

import com.tcc.seboonline.modelos.ImagensLivros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagensLivrosRepository extends JpaRepository<ImagensLivros, Integer> {
    
}
