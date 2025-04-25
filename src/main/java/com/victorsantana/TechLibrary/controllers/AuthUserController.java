package com.victorsantana.TechLibrary.controllers;

import com.victorsantana.TechLibrary.dtos.AuthRequest;
import com.victorsantana.TechLibrary.dtos.AuthResponse;
import com.victorsantana.TechLibrary.useCases.user.AuthUserUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/auth")
public class AuthUserController {

    private final AuthUserUseCase authUserUseCase;

    public AuthUserController(AuthUserUseCase authUserUseCase) { this.authUserUseCase = authUserUseCase; }

    @PostMapping
    public ResponseEntity<AuthResponse> execute(@RequestBody AuthRequest dto) {
        AuthResponse result = this.authUserUseCase.execute(dto);
        return ResponseEntity.ok().body(result);
    }
}