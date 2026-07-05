package com.wellness360.taskmanager.service;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.TaskResponse;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.exception.TaskNotFoundException;
import com.wellness360.taskmanager.mapper.TaskMapper;
import com.wellness360.taskmanager.model.Task;
import com.wellness360.taskmanager.repository.TaskRepository;
import com.wellness360.taskmanager.util.StatusTransitionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        log.info("Retrieving all tasks, count={}", taskRepository.count());
        return taskRepository.findAll().stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(UUID id) {
        Task task = findTaskOrThrow(id);
        log.info("Retrieved task id={}", id);
        return taskMapper.toResponse(task);
    }

    @Override
    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = taskMapper.toEntity(request);
        if (request.getStatus() != null && request.getStatus() != TaskStatus.PENDING) {
            StatusTransitionValidator.validateTransition(TaskStatus.PENDING, request.getStatus());
        }
        Task saved = taskRepository.save(task);
        log.info("Created task id={} status={}", saved.getId(), saved.getStatus());
        return taskMapper.toResponse(saved);
    }

    @Override
    public TaskResponse updateTask(UUID id, UpdateTaskRequest request) {
        Task existing = findTaskOrThrow(id);
        StatusTransitionValidator.validateTransition(existing.getStatus(), request.getStatus());
        taskMapper.updateEntity(existing, request);
        Task updated = taskRepository.save(existing);
        log.info("Updated task id={} status={}", id, updated.getStatus());
        return taskMapper.toResponse(updated);
    }

    @Override
    public void deleteTask(UUID id) {
        if (!taskRepository.deleteById(id)) {
            throw new TaskNotFoundException(id);
        }
        log.info("Deleted task id={}", id);
    }

    @Override
    public TaskResponse completeTask(UUID id) {
        Task task = findTaskOrThrow(id);
        if (task.getStatus() == TaskStatus.COMPLETED) {
            log.info("Task id={} is already completed; returning idempotent response", id);
            return taskMapper.toResponse(task);
        }
        StatusTransitionValidator.validateTransition(task.getStatus(), TaskStatus.COMPLETED);
        task.setStatus(TaskStatus.COMPLETED);
        task.setUpdatedAt(java.time.Instant.now());
        Task completed = taskRepository.save(task);
        log.info("Completed task id={}", id);
        return taskMapper.toResponse(completed);
    }

    private Task findTaskOrThrow(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }
}
