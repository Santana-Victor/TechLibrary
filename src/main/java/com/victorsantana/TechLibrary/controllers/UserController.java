package com.victorsantana.TechLibrary.controllers;

import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setId(UUID.randomUUID());
        user.setCreatedAt(Instant.now());

        User savedUser = this.userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}