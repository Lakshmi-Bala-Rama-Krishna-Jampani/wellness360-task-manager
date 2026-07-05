package com.wellness360.taskmanager.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Health endpoint is accessible without authentication")
    void healthEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tasks endpoint requires authentication")
    void tasksEndpointRequiresAuth() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Tasks endpoint accepts valid basic auth credentials")
    void tasksEndpointWithValidCredentials() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", basicAuth("testuser", "testpass")))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Tasks endpoint rejects invalid credentials")
    void tasksEndpointWithInvalidCredentials() throws Exception {
        mockMvc.perform(get("/tasks")
                        .header("Authorization", basicAuth("testuser", "wrong")))
                .andExpect(status().isUnauthorized());
    }

    private String basicAuth(String username, String password) {
        String token = java.util.Base64.getEncoder()
                .encodeToString((username + ":" + password).getBytes());
        return "Basic " + token;
    }
}
