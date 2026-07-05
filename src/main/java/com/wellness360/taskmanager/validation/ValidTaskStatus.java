package com.wellness360.taskmanager.validation;

import com.wellness360.taskmanager.enums.TaskStatus;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ValidTaskStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskStatus {

    String message() default "Invalid task status";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    TaskStatus[] allowed() default {TaskStatus.PENDING, TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED};
}
