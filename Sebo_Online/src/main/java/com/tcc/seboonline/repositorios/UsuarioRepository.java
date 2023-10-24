package com.tcc.seboonline.repositorios;

import com.tcc.seboonline.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmailAndPassword(String email, String password);

    Optional<Usuario> findByEmail(String email);

    Optional<List<Usuario>> findAllByFirstNameAndLastName(String firstName, String lastName);

    @Query(
        "SELECT u FROM Usuario u " +
            "WHERE lower(u.firstName) LIKE '%'||lower(?1)||'%' AND lower(u.lastName) LIKE '%'||lower(?2)||'%' " +
                "OR lower(u.firstName) LIKE '%'||lower(?2)||'%' AND lower(u.lastName) LIKE '%'||lower(?1)||'%'"
    )
    Optional<List<Usuario>> findAllMatchesByFirstAndLastNames(String firstName, String lastName);

    @Query(
        "SELECT u FROM Usuario u " +
            "WHERE lower(u.firstName) LIKE '%'||lower(?1)||'%' OR lower(u.lastName) LIKE '%'||lower(?1)||'%' "
    )
    Optional<List<Usuario>>findAllMatchesByEitherFirstOrLastName(String name);
}