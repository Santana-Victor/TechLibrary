package com.victorsantana.TechLibrary.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.victorsantana.TechLibrary.dtos.user.CreateUserRequest;
import com.victorsantana.TechLibrary.utils.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateUserIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private TestUtils testUtils;

    @Test
    @DisplayName("Should return CREATED (201) and register user successfully.")
    void case01() throws Exception {
        CreateUserRequest input = new CreateUserRequest("John Doe",
                "johnDoe@example.com",
                "12345");
        String jsonRequest = mapper.writeValueAsString(input);

        String REGEX_EMAIL_ADDRESS_FORMAT = "^[\\p{L}0-9._-]+@([a-zA-Z0-9]+\\.)+[a-zA-Z]{2,}$";
        String REGEX_UUID_V4 = "^[0-9a-fA-F-]{36}$";
        String REGEX_TIMESTAMP_WITH_OFFSET = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}([+-]\\d{2}:\\d{2})$";

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matchesPattern(REGEX_UUID_V4)))
                .andExpect(jsonPath("$.name").isNotEmpty())
                .andExpect(jsonPath("$.email").value(matchesPattern(REGEX_EMAIL_ADDRESS_FORMAT)))
                .andExpect(jsonPath("$.createdAt").value(matchesPattern(REGEX_TIMESTAMP_WITH_OFFSET)));
    }

    @Test
    @DisplayName("Should return CONFLICT (409) when email address is already registered.")
    void case02() throws Exception {
        CreateUserRequest input = new CreateUserRequest("Jane Smith",
                "jane.smith@example.com",
                "Password123!");
        String jsonRequest = mapper.writeValueAsString(input);

        this.testUtils.createUser(input);

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Email address already registered."));
    }
}