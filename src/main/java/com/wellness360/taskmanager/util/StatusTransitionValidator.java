package com.wellness360.taskmanager.util;

import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.exception.InvalidStatusTransitionException;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Validates allowed task status transitions.
 */
public final class StatusTransitionValidator {

    private static final Map<TaskStatus, Set<TaskStatus>> ALLOWED_TRANSITIONS = Map.of(
            TaskStatus.PENDING, EnumSet.of(TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED),
            TaskStatus.IN_PROGRESS, EnumSet.of(TaskStatus.COMPLETED, TaskStatus.PENDING),
            TaskStatus.COMPLETED, EnumSet.noneOf(TaskStatus.class)
    );

    private StatusTransitionValidator() {
    }

    public static void validateTransition(TaskStatus current, TaskStatus target) {
        if (current == target) {
            return;
        }
        Set<TaskStatus> allowed = ALLOWED_TRANSITIONS.get(current);
        if (allowed == null || !allowed.contains(target)) {
            throw new InvalidStatusTransitionException(
                    String.format("Invalid status transition from %s to %s", current, target));
        }
    }
}
