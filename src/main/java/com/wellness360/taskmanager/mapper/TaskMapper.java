package com.wellness360.taskmanager.mapper;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.LinkDto;
import com.wellness360.taskmanager.dto.TaskResponse;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.model.Task;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class TaskMapper {

    public Task toEntity(CreateTaskRequest request) {
        Instant now = Instant.now();
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle().trim());
        task.setDescription(normalizeDescription(request.getDescription()));
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus() != null ? request.getStatus() : TaskStatus.PENDING);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return task;
    }

    public void updateEntity(Task task, UpdateTaskRequest request) {
        task.setTitle(request.getTitle().trim());
        task.setDescription(normalizeDescription(request.getDescription()));
        task.setDueDate(request.getDueDate());
        task.setStatus(request.getStatus());
        task.setUpdatedAt(Instant.now());
    }

    public TaskResponse toResponse(Task task) {
        return toResponse(task, true);
    }

    public TaskResponse toResponse(Task task, boolean includeLinks) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setDueDate(task.getDueDate());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        if (includeLinks) {
            response.setLinks(buildLinks(task));
        }
        return response;
    }

    private Map<String, LinkDto> buildLinks(Task task) {
        String basePath = "/tasks/" + task.getId();
        Map<String, LinkDto> links = new LinkedHashMap<>();
        links.put("self", new LinkDto(basePath, "GET"));
        links.put("update", new LinkDto(basePath, "PUT"));
        links.put("delete", new LinkDto(basePath, "DELETE"));
        if (task.getStatus() != TaskStatus.COMPLETED) {
            links.put("complete", new LinkDto(basePath + "/complete", "PATCH"));
        }
        links.put("collection", new LinkDto("/tasks", "GET"));
        return links;
    }

    private String normalizeDescription(String description) {
        if (description == null) {
            return null;
        }
        String trimmed = description.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
