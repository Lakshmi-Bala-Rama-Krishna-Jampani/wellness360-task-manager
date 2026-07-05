package com.wellness360.taskmanager.validation;

import com.wellness360.taskmanager.dto.CreateTaskRequest;
import com.wellness360.taskmanager.dto.UpdateTaskRequest;
import com.wellness360.taskmanager.enums.TaskStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("CreateTaskRequest rejects blank title")
    void createTaskBlankTitle() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("   ");
        request.setDueDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    @DisplayName("CreateTaskRequest rejects null due date")
    void createTaskNullDueDate() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("dueDate"));
    }

    @Test
    @DisplayName("CreateTaskRequest rejects past due date")
    void createTaskPastDueDate() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("Valid title");
        request.setDueDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("dueDate"));
    }

    @Test
    @DisplayName("CreateTaskRequest rejects oversized title")
    void createTaskOversizedTitle() {
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("x".repeat(201));
        request.setDueDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<CreateTaskRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("title"));
    }

    @Test
    @DisplayName("UpdateTaskRequest requires status")
    void updateTaskMissingStatus() {
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTitle("Title");
        request.setDueDate(LocalDate.now());

        Set<ConstraintViolation<UpdateTaskRequest>> violations = validator.validate(request);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("status"));
    }

    @Test
    @DisplayName("ValidTaskStatusValidator accepts allowed status")
    void validTaskStatus() throws NoSuchFieldException {
        ValidTaskStatusValidator statusValidator = new ValidTaskStatusValidator();
        ValidTaskStatus annotation = ValidTaskStatusHolder.class
                .getDeclaredField("status")
                .getAnnotation(ValidTaskStatus.class);
        statusValidator.initialize(annotation);

        assertThat(statusValidator.isValid(TaskStatus.PENDING, null)).isTrue();
        assertThat(statusValidator.isValid(TaskStatus.COMPLETED, null)).isTrue();
    }

    private static class ValidTaskStatusHolder {
        @ValidTaskStatus
        private TaskStatus status;
    }
}
