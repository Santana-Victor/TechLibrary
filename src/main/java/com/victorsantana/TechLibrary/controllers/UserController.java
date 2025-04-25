package com.victorsantana.TechLibrary.controllers;

import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.useCases.user.CreateUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase) {
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        User result = this.createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}