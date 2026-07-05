package com.wellness360.taskmanager.advice;

import com.wellness360.taskmanager.exception.BadRequestException;
import com.wellness360.taskmanager.exception.InvalidStatusTransitionException;
import com.wellness360.taskmanager.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestExceptionController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @DisplayName("Should return standardized 404 error response")
    void handleTaskNotFound() throws Exception {
        mockMvc.perform(get("/test-exceptions/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/test-exceptions/not-found"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @WithMockUser
    @DisplayName("Should return standardized 400 error response")
    void handleBadRequest() throws Exception {
        mockMvc.perform(get("/test-exceptions/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid input"));
    }

    @Test
    @WithMockUser
    @DisplayName("Should return standardized error for invalid status transition")
    void handleInvalidStatusTransition() throws Exception {
        mockMvc.perform(get("/test-exceptions/invalid-transition"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid transition"));
    }

}
