package com.tcc.seboonline.repositorios;

import java.util.Optional;

import com.tcc.seboonline.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.seboonline.modelos.PerfilUsuario;


@Repository
public interface PerfilUsuarioRepository extends JpaRepository<PerfilUsuario, Integer> {
    Optional<PerfilUsuario> findByOwner(Usuario user);
}
