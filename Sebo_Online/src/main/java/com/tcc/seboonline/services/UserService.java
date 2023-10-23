package com.tcc.seboonline.services;

import com.tcc.seboonline.exceptions.EmailReservedException;
import com.tcc.seboonline.models.User;
import com.tcc.seboonline.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByCredentials(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    public User save(User user) throws EmailReservedException {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EmailReservedException("O " + user.getEmail() + " e-mail já foi usado.");

        return userRepository.save(user);
    }

    public Optional<User> findById(int id) {
        return this.userRepository.findById(id);
    }
}
