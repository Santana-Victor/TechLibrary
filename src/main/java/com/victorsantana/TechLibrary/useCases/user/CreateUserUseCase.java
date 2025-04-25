package com.victorsantana.TechLibrary.useCases.user;

import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.dtos.user.CreateUserResponse;
import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.exceptions.EmailAddressAlreadyRegisteredException;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateUserUseCase {

    private final UserRepository userRepository;

    public CreateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse execute(CreateUserRequest dto) {
        this.userRepository
                .findByEmail(dto.email())
                .ifPresent(userFound -> { throw new EmailAddressAlreadyRegisteredException(); });

        User newUser = new User(UUID.randomUUID(), dto.name(), dto.email(), dto.password(), Instant.now());

        User savedUser = this.userRepository.save(newUser);

        return new CreateUserResponse(savedUser);
    }
}