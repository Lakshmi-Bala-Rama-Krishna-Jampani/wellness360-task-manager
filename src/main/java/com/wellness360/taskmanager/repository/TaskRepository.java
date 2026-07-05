package com.wellness360.taskmanager.repository;

import com.wellness360.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(UUID id);

    List<Task> findAll();

    boolean existsById(UUID id);

    boolean deleteById(UUID id);

    long count();
}
