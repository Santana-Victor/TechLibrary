package com.victorsantana.TechLibrary.controllers;

import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.dtos.user.CreateUserResponse;
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

    public UserController(CreateUserUseCase createUserUseCase) { this.createUserUseCase = createUserUseCase; }

    @PostMapping
    public ResponseEntity<CreateUserResponse> create(@RequestBody CreateUserRequest dto) {
        CreateUserResponse result = this.createUserUseCase.execute(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}