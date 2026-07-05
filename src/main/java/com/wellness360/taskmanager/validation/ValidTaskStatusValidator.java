package com.wellness360.taskmanager.validation;

import com.wellness360.taskmanager.enums.TaskStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidTaskStatusValidator implements ConstraintValidator<ValidTaskStatus, TaskStatus> {

    private Set<TaskStatus> allowedStatuses;

    @Override
    public void initialize(ValidTaskStatus constraintAnnotation) {
        allowedStatuses = Arrays.stream(constraintAnnotation.allowed()).collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(TaskStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return allowedStatuses.contains(value);
    }
}
