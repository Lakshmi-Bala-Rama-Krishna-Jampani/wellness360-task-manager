package com.wellness360.taskmanager.service;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.TaskResponse;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    List<TaskResponse> getAllTasks();

    TaskResponse getTaskById(UUID id);

    TaskResponse createTask(CreateTaskRequest request);

    TaskResponse updateTask(UUID id, UpdateTaskRequest request);

    void deleteTask(UUID id);

    TaskResponse completeTask(UUID id);
}
