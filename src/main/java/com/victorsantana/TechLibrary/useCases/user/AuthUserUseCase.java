package com.victorsantana.TechLibrary.useCases.user;

import com.victorsantana.TechLibrary.dtos.AuthRequest;
import com.victorsantana.TechLibrary.dtos.AuthResponse;
import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.exceptions.InvalidCredentialsException;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import com.victorsantana.TechLibrary.security.JwtProvider;
import com.victorsantana.TechLibrary.utils.TimestampGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class AuthUserUseCase {

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final TimestampGenerator timestampGenerator;
    private final UserRepository userRepository;

    public AuthUserUseCase(JwtProvider jwtProvider,
                           PasswordEncoder passwordEncoder,
                           TimestampGenerator timestampGenerator,
                           UserRepository userRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.timestampGenerator = timestampGenerator;
        this.userRepository = userRepository;
    }

    public AuthResponse execute(AuthRequest dto) {
        User userFound = this.userRepository
                .findByEmail(dto.email())
                .orElseThrow(InvalidCredentialsException::new);

        boolean isPasswordCorrect = this.passwordEncoder.matches(dto.password(), userFound.getPassword());

        if (isPasswordCorrect == false) throw new InvalidCredentialsException();

        Instant createdAt = this.timestampGenerator.now();
        Instant expiresAt = createdAt.plus(Duration.ofHours(2));

        String token = this.jwtProvider.generateToken(createdAt, expiresAt, userFound.getId());

        return new AuthResponse(token, expiresAt.toEpochMilli());
    }
}