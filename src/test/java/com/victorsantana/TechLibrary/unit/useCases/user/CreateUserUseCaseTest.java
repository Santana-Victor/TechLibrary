package com.victorsantana.TechLibrary.unit.useCases.user;

import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.dtos.user.CreateUserResponse;
import com.victorsantana.TechLibrary.entities.User;
import com.victorsantana.TechLibrary.exceptions.EmailAddressAlreadyRegisteredException;
import com.victorsantana.TechLibrary.repositories.UserRepository;
import com.victorsantana.TechLibrary.useCases.user.CreateUserUseCase;
import com.victorsantana.TechLibrary.utils.TimestampGenerator;
import com.victorsantana.TechLibrary.utils.UUIDGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    private final UUID userId = UUID.fromString("1babbc55-8000-75d5-bb90-b952514a072d");
    private final Instant creationTimestamp = Instant.parse("2030-03-03T09:00:00Z");

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TimestampGenerator timestampGenerator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UUIDGenerator uuuidGenerator;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    @DisplayName("Should register the user successfully.")
    void case01() {
        String encodedPassword = "encodedPassword";
        CreateUserRequest input = new CreateUserRequest("John Doe",
                "johnDoe@example.com",
                "12345");
        User expectedUser = new User(userId,
                input.name(),
                input.email(),
                encodedPassword,
                creationTimestamp);

        doReturn(Optional.empty()).when(userRepository).findByEmail(eq(input.email()));
        doReturn(userId).when(uuuidGenerator).randomUUID();
        doReturn(creationTimestamp).when(timestampGenerator).now();
        doReturn(encodedPassword).when(passwordEncoder).encode(eq(input.password()));
        doReturn(expectedUser).when(userRepository).save(any(User.class));

        CreateUserResponse output = createUserUseCase.execute(input);

        verify(userRepository).findByEmail(eq(input.email()));
        verify(uuuidGenerator).randomUUID();
        verify(timestampGenerator).now();
        verify(passwordEncoder).encode(eq(input.password()));
        verify(userRepository).save(userArgumentCaptor.capture());
        verifyNoMoreInteractions(userRepository, uuuidGenerator, timestampGenerator, passwordEncoder);

        User userCaptured = userArgumentCaptor.getValue();

        assertThat(output).withFailMessage("Output must not be null.").isNotNull();
        assertThat(userCaptured).withFailMessage("User captured must not be null.").isNotNull();

        assertThat(output).usingRecursiveComparison().isNotNull();
        assertThat(userCaptured).usingRecursiveComparison().isNotNull();

        assertThat(userCaptured)
                .withFailMessage("Expected: %s\nReceived: %s".formatted(expectedUser, userCaptured))
                .isEqualTo(expectedUser);
    }


    @Test
    @DisplayName("Should throw EmailAddressAlreadyRegisteredException when the given email address is already registered.")
    void case02() {
        String encodedPassword = "encodedPassword2";
        CreateUserRequest input = new CreateUserRequest("John Doe",
                "johnDoe@example.com",
                "12345");
        User existingUser = new User(userId,
                input.name(),
                input.email(),
                encodedPassword,
                creationTimestamp);

        doReturn(Optional.of(existingUser)).when(userRepository).findByEmail(eq(input.email()));

        assertThrows(EmailAddressAlreadyRegisteredException.class, () -> createUserUseCase.execute(input));

        verify(userRepository).findByEmail(eq(input.email()));
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(uuuidGenerator, timestampGenerator, passwordEncoder);
    }
}