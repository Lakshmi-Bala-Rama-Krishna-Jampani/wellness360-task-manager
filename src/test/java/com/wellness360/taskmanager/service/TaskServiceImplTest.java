package com.wellness360.taskmanager.service;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.exception.InvalidStatusTransitionException;
import com.wellness360.taskmanager.exception.TaskNotFoundException;
import com.wellness360.taskmanager.mapper.TaskMapper;
import com.wellness360.taskmanager.model.Task;
import com.wellness360.taskmanager.repository.InMemoryTaskRepository;
import com.wellness360.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TaskServiceImplTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = new InMemoryTaskRepository();
        taskService = new TaskServiceImpl(taskRepository, new TaskMapper());
    }

    @Test
    @DisplayName("Should create task with default PENDING status")
    void shouldCreateTask() {
        CreateTaskRequest request = buildCreateRequest("New Task", LocalDate.now().plusDays(1));

        var response = taskService.createTask(request);

        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("New Task");
        assertThat(response.getStatus()).isEqualTo(TaskStatus.PENDING);
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
        assertThat(taskRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should retrieve task by id")
    void shouldGetTaskById() {
        var created = taskService.createTask(buildCreateRequest("Task", LocalDate.now().plusDays(2)));

        var found = taskService.getTaskById(created.getId());

        assertThat(found.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("Should throw when task not found")
    void shouldThrowWhenTaskNotFound() {
        UUID missingId = UUID.randomUUID();

        assertThatThrownBy(() -> taskService.getTaskById(missingId))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(missingId.toString());
    }

    @Test
    @DisplayName("Should update existing task and refresh updatedAt")
    void shouldUpdateTask() throws InterruptedException {
        var created = taskService.createTask(buildCreateRequest("Original", LocalDate.now().plusDays(3)));
        Thread.sleep(5);

        UpdateTaskRequest update = new UpdateTaskRequest();
        update.setTitle("Updated");
        update.setDescription("Updated description");
        update.setDueDate(LocalDate.now().plusDays(5));
        update.setStatus(TaskStatus.IN_PROGRESS);

        var updated = taskService.updateTask(created.getId(), update);

        assertThat(updated.getTitle()).isEqualTo("Updated");
        assertThat(updated.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(created.getUpdatedAt());
    }

    @Test
    @DisplayName("Should reject invalid status transition on update")
    void shouldRejectInvalidStatusTransition() {
        CreateTaskRequest createRequest = buildCreateRequest("Task", LocalDate.now().plusDays(1));
        createRequest.setStatus(TaskStatus.COMPLETED);
        var created = taskService.createTask(createRequest);

        UpdateTaskRequest update = new UpdateTaskRequest();
        update.setTitle("Task");
        update.setDueDate(LocalDate.now().plusDays(1));
        update.setStatus(TaskStatus.PENDING);

        assertThatThrownBy(() -> taskService.updateTask(created.getId(), update))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    @DisplayName("Should delete existing task")
    void shouldDeleteTask() {
        var created = taskService.createTask(buildCreateRequest("Delete me", LocalDate.now().plusDays(1)));

        taskService.deleteTask(created.getId());

        assertThat(taskRepository.count()).isZero();
        assertThatThrownBy(() -> taskService.getTaskById(created.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Should throw when deleting missing task")
    void shouldThrowWhenDeletingMissingTask() {
        UUID missingId = UUID.randomUUID();

        assertThatThrownBy(() -> taskService.deleteTask(missingId))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    @DisplayName("Should complete task")
    void shouldCompleteTask() {
        var created = taskService.createTask(buildCreateRequest("Complete me", LocalDate.now().plusDays(1)));

        var completed = taskService.completeTask(created.getId());

        assertThat(completed.getStatus()).isEqualTo(TaskStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should return idempotent response when completing already completed task")
    void shouldCompleteAlreadyCompletedTaskIdempotently() {
        CreateTaskRequest request = buildCreateRequest("Done", LocalDate.now().plusDays(1));
        request.setStatus(TaskStatus.COMPLETED);
        var created = taskService.createTask(request);

        var result = taskService.completeTask(created.getId());

        assertThat(result.getStatus()).isEqualTo(TaskStatus.COMPLETED);
        assertThat(result.getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("Should not complete deleted task")
    void shouldNotCompleteDeletedTask() {
        var created = taskService.createTask(buildCreateRequest("Ephemeral", LocalDate.now().plusDays(1)));
        taskService.deleteTask(created.getId());

        assertThatThrownBy(() -> taskService.completeTask(created.getId()))
                .isInstanceOf(TaskNotFoundException.class);
    }

    private CreateTaskRequest buildCreateRequest(String title, LocalDate dueDate) {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle(title);
        request.setDueDate(dueDate);
        return request;
    }
}
