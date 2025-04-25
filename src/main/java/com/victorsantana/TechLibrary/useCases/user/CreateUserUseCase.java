package com.victorsantana.TechLibrary.useCases.user;

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
    public User execute(User user) {
        user.setId(UUID.randomUUID());
        user.setCreatedAt(Instant.now());

        this.userRepository
                .findByEmail(user.getEmail())
                .ifPresent(userFound -> { throw new EmailAddressAlreadyRegisteredException(); });

        return this.userRepository.save(user);
    }
}