package com.victorsantana.TechLibrary.unit.useCases.user;

import com.victorsantana.TechLibrary.dtos.AuthRequest;
import com.victorsantana.TechLibrary.dtos.AuthResponse;
import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.exceptions.InvalidCredentialsException;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import com.victorsantana.TechLibrary.security.JwtProvider;
import com.victorsantana.TechLibrary.useCases.user.AuthUserUseCase;
import com.victorsantana.TechLibrary.utils.TimestampGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class AuthUserUseCaseTest {

    private final String name = "John Doe";
    private final String email = "johnDoe@example.com";
    private final String password = "12345";
    private final String encodedPassword = "encodedPassword";
    private final UUID userId = UUID.fromString("1babbc55-8000-75d5-bb90-b952514a072d");
    private final Instant creationTimestamp = Instant.parse("2030-03-03T09:00:00Z");

    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TimestampGenerator timestampGenerator;

    @InjectMocks
    private AuthUserUseCase authUserUseCase;

    @Test
    @DisplayName("Should authenticate the user successfully.")
    void case01() {
        String expectedToken = "token-for-user";
        Instant tokenCreatedAt = Instant.parse("2030-03-03T09:01:00Z");
        Instant tokenExpiresAt = tokenCreatedAt.plus(Duration.ofHours(2));
        Long tokenExpirationTimeInEpochMilli = tokenExpiresAt.toEpochMilli();

        AuthRequest input = new AuthRequest(email, password);
        User user = new User(userId, name, email, encodedPassword, creationTimestamp);

        doReturn(Optional.of(user)).when(userRepository).findByEmail(eq(input.email()));
        doReturn(true).when(passwordEncoder).matches(eq(input.password()), eq(user.getPassword()));
        doReturn(tokenCreatedAt).when(timestampGenerator).now();
        doReturn(expectedToken).when(jwtProvider).generateToken(eq(tokenCreatedAt), eq(tokenExpiresAt), eq(userId));

        AuthResponse output = authUserUseCase.execute(input);

        verify(userRepository).findByEmail(eq(input.email()));
        verify(passwordEncoder).matches(eq(input.password()), eq(user.getPassword()));
        verify(timestampGenerator).now();
        verify(jwtProvider).generateToken(eq(tokenCreatedAt), eq(tokenExpiresAt), eq(userId));
        verifyNoMoreInteractions(userRepository, passwordEncoder, timestampGenerator, jwtProvider);

        assertThat(output).withFailMessage("Output must not be null.").isNotNull();
        assertThat(output).usingRecursiveComparison().isNotNull();

        assertThat(output.token())
                .withFailMessage("Expected: %s\nReceived: %s".formatted(expectedToken, output.token()))
                .isEqualTo(expectedToken);
        assertThat(output.expiresAt())
                .withFailMessage("Expected: %d\nReceived: %d".formatted(tokenExpirationTimeInEpochMilli,
                        output.expiresAt()))
                .isEqualTo(tokenExpirationTimeInEpochMilli);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when the given email address is not registered.")
    void case02() {
        AuthRequest input = new AuthRequest(email, password);

        doReturn(Optional.empty()).when(userRepository).findByEmail(eq(email));

        assertThrows(InvalidCredentialsException.class, () -> authUserUseCase.execute(input));

        verify(userRepository).findByEmail(eq(email));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, timestampGenerator, jwtProvider);
    }

    @Test
    @DisplayName("Should throw InvalidCredentialsException when the given password is incorrect.")
    void case03() {
        AuthRequest input = new AuthRequest(email, password);
        User user = new User(userId, name, email, encodedPassword, creationTimestamp);

        doReturn(Optional.of(user)).when(userRepository).findByEmail(eq(email));
        doReturn(false).when(passwordEncoder).matches(eq(input.password()), eq(user.getPassword()));

        assertThrows(InvalidCredentialsException.class, () -> authUserUseCase.execute(input));

        verify(userRepository).findByEmail(eq(email));
        verify(passwordEncoder).matches(eq(input.password()), eq(user.getPassword()));
        verifyNoMoreInteractions(userRepository, passwordEncoder);
        verifyNoInteractions(timestampGenerator, jwtProvider);
    }
}