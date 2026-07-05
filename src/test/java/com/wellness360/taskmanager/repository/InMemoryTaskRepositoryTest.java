package com.wellness360.taskmanager.repository;

import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryTaskRepository();
    }

    @Test
    @DisplayName("Should save and retrieve task by id")
    void saveAndFind() {
        Task task = buildTask(UUID.randomUUID());

        repository.save(task);

        assertThat(repository.findById(task.getId())).contains(task);
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("Should delete existing task")
    void deleteExisting() {
        Task task = buildTask(UUID.randomUUID());
        repository.save(task);

        assertThat(repository.deleteById(task.getId())).isTrue();
        assertThat(repository.findById(task.getId())).isEmpty();
    }

    @Test
    @DisplayName("Should return false when deleting missing task")
    void deleteMissing() {
        assertThat(repository.deleteById(UUID.randomUUID())).isFalse();
    }

    @Test
    @DisplayName("Should handle concurrent saves safely")
    void concurrentSaves() throws InterruptedException {
        int threadCount = 20;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    Task task = buildTask(UUID.randomUUID());
                    repository.save(task);
                    successCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertThat(successCount.get()).isEqualTo(threadCount);
        assertThat(repository.count()).isEqualTo(threadCount);
    }

    private Task buildTask(UUID id) {
        Instant now = Instant.now();
        Task task = new Task();
        task.setId(id);
        task.setTitle("Concurrent Task");
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return task;
    }
}
