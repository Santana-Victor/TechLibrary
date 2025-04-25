package com.victorsantana.TechLibrary.utils;

import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.useCases.user.CreateUserUseCase;
import org.springframework.stereotype.Component;

@Component
public class TestUtils {

    private final CreateUserUseCase createUserUseCase;

    public TestUtils(CreateUserUseCase createUserUseCase) { this.createUserUseCase = createUserUseCase; }

    public void createUser(CreateUserRequest dto) { this.createUserUseCase.execute(dto); }
}