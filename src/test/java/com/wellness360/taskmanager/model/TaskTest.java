package com.wellness360.taskmanager.model;

import com.wellness360.taskmanager.enums.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TaskTest {

    @Test
    @DisplayName("Should implement equals and hashCode based on id")
    void equalsAndHashCode() {
        UUID id = UUID.randomUUID();
        Task first = buildTask(id, "First");
        Task second = buildTask(id, "Second");
        Task different = buildTask(UUID.randomUUID(), "First");

        assertThat(first).isEqualTo(second);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
        assertThat(first).isNotEqualTo(different);
        assertThat(first).isNotEqualTo(null);
        assertThat(first).isNotEqualTo("not-a-task");
    }

    private Task buildTask(UUID id, String title) {
        Instant now = Instant.now();
        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        return task;
    }
}
