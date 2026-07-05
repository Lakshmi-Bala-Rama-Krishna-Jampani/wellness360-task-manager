package com.wellness360.taskmanager.mapper;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    private final TaskMapper mapper = new TaskMapper();

    @Test
    @DisplayName("Should map create request to entity with trimmed title")
    void toEntity() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("  Trimmed Title  ");
        request.setDescription("  Description  ");
        request.setDueDate(LocalDate.now().plusDays(1));

        Task task = mapper.toEntity(request);

        assertThat(task.getId()).isNotNull();
        assertThat(task.getTitle()).isEqualTo("Trimmed Title");
        assertThat(task.getDescription()).isEqualTo("Description");
        assertThat(task.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(task.getCreatedAt()).isNotNull();
        assertThat(task.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should include HATEOAS links in response")
    void toResponseWithLinks() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Link Test");
        request.setDueDate(LocalDate.now().plusDays(1));
        Task task = mapper.toEntity(request);

        var response = mapper.toResponse(task);

        assertThat(response.getLinks()).containsKeys("self", "update", "delete", "complete", "collection");
        assertThat(response.getLinks().get("self").getMethod()).isEqualTo("GET");
    }
}
