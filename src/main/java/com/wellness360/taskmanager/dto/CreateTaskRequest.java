package com.wellness360.taskmanager.dto;

import com.wellness360.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request payload for creating a new task")
public class CreateTaskRequest {

    @NotBlank(message = "Title is required and cannot be blank")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Task title", example = "Complete architecture review", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    @Schema(description = "Task description", example = "Review system design document")
    private String description;

    @NotNull(message = "Due date is required")
    @FutureOrPresent(message = "Due date must be today or in the future")
    @Schema(description = "Due date", example = "2026-07-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dueDate;

    @Schema(description = "Initial status (defaults to PENDING)", example = "PENDING")
    private TaskStatus status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
