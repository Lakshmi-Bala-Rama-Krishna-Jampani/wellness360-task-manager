package com.wellness360.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wellness360.taskmanager.enums.TaskStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Task representation returned by the API")
public class TaskResponse {

    @Schema(description = "Unique task identifier", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Task title", example = "Complete architecture review")
    private String title;

    @Schema(description = "Detailed task description", example = "Review system design document and provide feedback")
    private String description;

    @Schema(description = "Due date in ISO-8601 format", example = "2026-07-15")
    private LocalDate dueDate;

    @Schema(description = "Current task status", example = "PENDING")
    private TaskStatus status;

    @Schema(description = "Creation timestamp in UTC", example = "2026-07-04T10:30:00Z")
    private Instant createdAt;

    @Schema(description = "Last update timestamp in UTC", example = "2026-07-04T10:30:00Z")
    private Instant updatedAt;

    @Schema(description = "HATEOAS links (REST Level 3)")
    private Map<String, LinkDto> links;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Map<String, LinkDto> getLinks() {
        return links;
    }

    public void setLinks(Map<String, LinkDto> links) {
        this.links = links;
    }
}
