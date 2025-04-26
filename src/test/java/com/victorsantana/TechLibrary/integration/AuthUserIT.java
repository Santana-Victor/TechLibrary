package com.victorsantana.TechLibrary.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorsantana.TechLibrary.dtos.AuthRequest;
import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthUserIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestUtils testUtils;

    @Test
    @DisplayName("Should return OK (200) and authenticate user successfully.")
    void case01() throws Exception {
        String name = "John Doe";
        String email = "johnDoe@example.com";
        String password = "12345";
        CreateUserRequest createUserRequest = new CreateUserRequest(name, email, password);
        AuthRequest input = new AuthRequest(email, password);

        this.testUtils.createUser(createUserRequest);

        String jsonRequest = mapper.writeValueAsString(input);

        mockMvc.perform(post("/users/auth")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.expiresAt").isNumber());
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED (401) when email address is not registered.")
    void case02() throws Exception {
        String email = "jane.smith@example.com";
        String password = "Password123!";

        AuthRequest input = new AuthRequest(email, password);

        String jsonRequest = mapper.writeValueAsString(input);

        mockMvc.perform(post("/users/auth")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Invalid credentials."));
    }

    @Test
    @DisplayName("Should return UNAUTHORIZED (401) when password is incorrect.")
    void case03() throws Exception {
        String name = "Alice Johnson";
        String email = "alice.johnson@example.com";
        String password = "MySecurePass456!";
        CreateUserRequest createUserRequest = new CreateUserRequest(name, email, password);
        AuthRequest input = new AuthRequest(email, password + "123");

        this.testUtils.createUser(createUserRequest);

        String jsonRequest = mapper.writeValueAsString(input);

        mockMvc.perform(post("/users/auth")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Invalid credentials."));
    }
}