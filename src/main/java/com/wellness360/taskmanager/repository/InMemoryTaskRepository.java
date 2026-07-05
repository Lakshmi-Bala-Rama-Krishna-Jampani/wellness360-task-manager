package com.wellness360.taskmanager.repository;

import com.wellness360.taskmanager.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe in-memory task repository backed by {@link ConcurrentHashMap}.
 */
@Repository
public class InMemoryTaskRepository implements TaskRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryTaskRepository.class);

    private final ConcurrentHashMap<UUID, Task> store = new ConcurrentHashMap<>();

    @Override
    public Task save(Task task) {
        store.put(task.getId(), task);
        log.debug("Saved task with id={}", task.getId());
        return task;
    }

    @Override
    public Optional<Task> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Task> findAll() {
        List<Task> tasks = new ArrayList<>(store.values());
        tasks.sort(Comparator.comparing(Task::getCreatedAt).reversed());
        return tasks;
    }

    @Override
    public boolean existsById(UUID id) {
        return store.containsKey(id);
    }

    @Override
    public boolean deleteById(UUID id) {
        Task removed = store.remove(id);
        if (removed != null) {
            log.debug("Deleted task with id={}", id);
            return true;
        }
        return false;
    }

    @Override
    public long count() {
        return store.size();
    }
}
