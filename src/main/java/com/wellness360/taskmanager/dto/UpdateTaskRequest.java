package com.wellness360.taskmanager.dto;

import com.wellness360.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Schema(description = "Request payload for updating an existing task")
public class UpdateTaskRequest {

    @NotBlank(message = "Title is required and cannot be blank")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    @Schema(description = "Task title", example = "Updated task title", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 5000, message = "Description must not exceed 5000 characters")
    @Schema(description = "Task description", example = "Updated description")
    private String description;

    @NotNull(message = "Due date is required")
    @Schema(description = "Due date (past dates allowed for overdue tasks)", example = "2026-07-15",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dueDate;

    @NotNull(message = "Status is required")
    @Schema(description = "Task status", example = "IN_PROGRESS", requiredMode = Schema.RequiredMode.REQUIRED)
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
