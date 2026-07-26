package com.example.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auth.repository.UserRepository;
import com.example.auth.model.User;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    // REGISTER
    public String register(User user) {

        // trim + basic validation
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setPassword(user.getPassword().trim());

        // Gmail validation
        if (!user.getEmail().endsWith("@gmail.com")) {
            return "Use Gmail only!";
        }

        // check duplicate username (important)
        Optional<User> existingUser = repo.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return "Username already exists!";
        }

        repo.save(user);
        return "Registered Successfully";
    }

    // LOGIN
    public User login(String username, String password) {

        Optional<User> userOpt = repo.findByUsername(username.trim());

        if (userOpt.isPresent()) {

            User user = userOpt.get();

            // trim fixes space issues
            if (user.getPassword().trim().equals(password.trim())) {
                return user;
            }
        }

        return null;
    }
}