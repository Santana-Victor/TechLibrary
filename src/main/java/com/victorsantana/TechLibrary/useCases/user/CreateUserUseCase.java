package com.victorsantana.TechLibrary.useCases.user;

import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.dtos.user.CreateUserResponse;
import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.exceptions.EmailAddressAlreadyRegisteredException;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import com.victorsantana.TechLibrary.utils.TimestampGenerator;
import com.victorsantana.TechLibrary.utils.UUIDGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class CreateUserUseCase {

    private final PasswordEncoder passwordEncoder;
    private final TimestampGenerator timestampGenerator;
    private final UUIDGenerator uuidGenerator;
    private final UserRepository userRepository;

    public CreateUserUseCase(PasswordEncoder passwordEncoder,
                             TimestampGenerator timestampGenerator,
                             UUIDGenerator uuidGenerator,
                             UserRepository userRepository
    ) {
        this.passwordEncoder = passwordEncoder;
        this.timestampGenerator = timestampGenerator;
        this.uuidGenerator = uuidGenerator;
        this.userRepository = userRepository;
    }

    @Transactional
    public CreateUserResponse execute(CreateUserRequest dto) {
        this.userRepository
                .findByEmail(dto.email())
                .ifPresent(userFound -> { throw new EmailAddressAlreadyRegisteredException(); });

        UUID userId = this.uuidGenerator.randomUUID();
        Instant createdAt = this.timestampGenerator.now();
        String encodedPassword = this.passwordEncoder.encode(dto.password());

        User newUser = new User(userId, dto.name(), dto.email(), encodedPassword, createdAt);

        User savedUser = this.userRepository.save(newUser);

        return new CreateUserResponse(savedUser);
    }
}