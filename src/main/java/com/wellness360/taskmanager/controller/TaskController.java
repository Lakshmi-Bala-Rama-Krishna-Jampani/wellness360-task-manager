package com.wellness360.taskmanager.controller;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.TaskResponse;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.service.TaskService;
import com.wellness360.taskmanager.util.UuidParser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Task management operations")
@SecurityRequirement(name = "basicAuth")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "List all tasks", description = "Retrieves all tasks sorted by creation date (newest first)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tasks retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get task by ID", description = "Retrieves a single task by its UUID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<TaskResponse> getTaskById(
            @Parameter(description = "Task UUID", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
            @PathVariable String id) {
        UUID taskId = UuidParser.parse(id);
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    @PostMapping
    @Operation(summary = "Create a task", description = "Creates a new task with validated input")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody CreateTaskRequest request) {
        TaskResponse created = taskService.createTask(request);
        return ResponseEntity
                .created(URI.create("/tasks/" + created.getId()))
                .body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task", description = "Fully updates an existing task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "400", description = "Validation or business rule error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String id,
            @Valid @RequestBody UpdateTaskRequest request) {
        UUID taskId = UuidParser.parse(id);
        return ResponseEntity.ok(taskService.updateTask(taskId, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a task", description = "Permanently removes a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        UUID taskId = UuidParser.parse(id);
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    @Operation(summary = "Complete a task", description = "Marks a task as COMPLETED (idempotent if already completed)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task completed or already completed"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID or status transition", content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<TaskResponse> completeTask(@PathVariable String id) {
        UUID taskId = UuidParser.parse(id);
        return ResponseEntity.ok(taskService.completeTask(taskId));
    }
}
