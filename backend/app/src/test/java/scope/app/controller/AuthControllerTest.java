package scope.app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import scope.app.domain.User;
import scope.app.repository.UserRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRegisterUser() throws Exception {

        String email = UUID.randomUUID() + "@test.com";

        String requestBody = """
                {
                  "email": "%s",
                  "password": "password123",
                  "fullName": "Test User"
                }
                """.formatted(email);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        assertTrue(
                userRepository.findByEmail(email).isPresent()
        );

        User user = userRepository.findByEmail(email).orElseThrow();

        assertEquals(email, user.getEmail());
        assertEquals("Test User", user.getFullName());
        assertNotNull(user.getPasswordHash());
        assertNotEquals("password123", user.getPasswordHash());
    }
}
