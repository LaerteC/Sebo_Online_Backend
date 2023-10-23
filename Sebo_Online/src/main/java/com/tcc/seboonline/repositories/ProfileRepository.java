package com.tcc.seboonline.repositories;

import java.util.Optional;

import com.tcc.seboonline.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tcc.seboonline.models.Profile;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Integer> {
    Optional<Profile> findByOwner(User user);
}
