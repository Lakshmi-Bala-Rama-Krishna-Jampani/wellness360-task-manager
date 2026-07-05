package com.wellness360.taskmanager.util;

import com.wellness360.taskmanager.exception.BadRequestException;

import java.util.UUID;

public final class UuidParser {

    private UuidParser() {
    }

    public static UUID parse(String rawId) {
        if (rawId == null || rawId.isBlank()) {
            throw new BadRequestException("Task id is required");
        }
        try {
            return UUID.fromString(rawId.trim());
        } catch (IllegalArgumentException ex) {
            throw new BadRequestException("Invalid UUID format: " + rawId);
        }
    }
}
