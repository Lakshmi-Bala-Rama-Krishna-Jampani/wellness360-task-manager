package com.wellness360.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wellness360.taskmanager.advice.GlobalExceptionHandler;
import com.wellness360.taskmanager.config.SecurityProperties;
import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.security.SecurityConfig;
import com.wellness360.taskmanager.dto.TaskResponse;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.service.TaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({GlobalExceptionHandler.class, SecurityConfig.class, SecurityProperties.class})
@ActiveProfiles("test")
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskService taskService;

    private static final UUID TASK_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");

    @Test
    @WithMockUser
    @DisplayName("GET /tasks returns task list")
    void getAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(buildResponse()));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(TASK_ID.toString()))
                .andExpect(jsonPath("$[0].title").value("Sample Task"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tasks/{id} returns task")
    void getTaskById() throws Exception {
        when(taskService.getTaskById(TASK_ID)).thenReturn(buildResponse());

        mockMvc.perform(get("/tasks/{id}", TASK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /tasks/{id} returns 400 for invalid UUID")
    void getTaskByInvalidId() throws Exception {
        mockMvc.perform(get("/tasks/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid UUID format: not-a-uuid"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /tasks creates task and returns 201")
    void createTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New Task");
        request.setDueDate(LocalDate.now().plusDays(1));

        when(taskService.createTask(any(CreateTaskRequest.class))).thenReturn(buildResponse());

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/tasks/" + TASK_ID))
                .andExpect(jsonPath("$.id").value(TASK_ID.toString()));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /tasks returns 400 for empty title")
    void createTaskValidationError() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("");
        request.setDueDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field_errors[0].field").value("title"));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /tasks/{id} updates task")
    void updateTask() throws Exception {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Updated");
        request.setDueDate(LocalDate.now().plusDays(2));
        request.setStatus(TaskStatus.IN_PROGRESS);

        TaskResponse updated = buildResponse();
        updated.setTitle("Updated");
        updated.setStatus(TaskStatus.IN_PROGRESS);
        when(taskService.updateTask(eq(TASK_ID), any(UpdateTaskRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/tasks/{id}", TASK_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated"));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /tasks/{id} returns 204")
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(TASK_ID);

        mockMvc.perform(delete("/tasks/{id}", TASK_ID))
                .andExpect(status().isNoContent());

        verify(taskService).deleteTask(TASK_ID);
    }

    @Test
    @WithMockUser
    @DisplayName("PATCH /tasks/{id}/complete marks task complete")
    void completeTask() throws Exception {
        TaskResponse completed = buildResponse();
        completed.setStatus(TaskStatus.COMPLETED);
        when(taskService.completeTask(TASK_ID)).thenReturn(completed);

        mockMvc.perform(patch("/tasks/{id}/complete", TASK_ID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    private TaskResponse buildResponse() {
        TaskResponse response = new TaskResponse();
        response.setId(TASK_ID);
        response.setTitle("Sample Task");
        response.setDescription("Description");
        response.setDueDate(LocalDate.now().plusDays(1));
        response.setStatus(TaskStatus.PENDING);
        response.setCreatedAt(Instant.parse("2026-07-04T10:00:00Z"));
        response.setUpdatedAt(Instant.parse("2026-07-04T10:00:00Z"));
        return response;
    }
}
