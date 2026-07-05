package com.wellness360.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Full task lifecycle integration test")
    void fullTaskLifecycle() throws Exception {
        CreateTaskRequest createRequest = new CreateTaskRequest();
        createRequest.setTitle("Integration Task");
        createRequest.setDescription("End-to-end test");
        createRequest.setDueDate(LocalDate.now().plusDays(7));

        MvcResult createResult = mockMvc.perform(post("/tasks")
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.links.self.href").exists())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String taskId = objectMapper.readTree(responseBody).get("id").asText();

        mockMvc.perform(get("/tasks/" + taskId)
                        .header("Authorization", authHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId));

        UpdateTaskRequest updateRequest = new UpdateTaskRequest();
        updateRequest.setTitle("Updated Integration Task");
        updateRequest.setDescription("Updated");
        updateRequest.setDueDate(LocalDate.now().plusDays(10));
        updateRequest.setStatus(TaskStatus.IN_PROGRESS);

        mockMvc.perform(put("/tasks/" + taskId)
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(patch("/tasks/" + taskId + "/complete")
                        .header("Authorization", authHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        mockMvc.perform(delete("/tasks/" + taskId)
                        .header("Authorization", authHeader()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/" + taskId)
                        .header("Authorization", authHeader()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should reject malformed JSON")
    void malformedJson() throws Exception {
        mockMvc.perform(post("/tasks")
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body"));
    }

    @Test
    @DisplayName("Should reject missing request body")
    void missingBody() throws Exception {
        mockMvc.perform(post("/tasks")
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    private String authHeader() {
        return "Basic " + java.util.Base64.getEncoder()
                .encodeToString("testuser:testpass".getBytes());
    }
}
