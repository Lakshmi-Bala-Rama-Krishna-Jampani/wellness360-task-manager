package com.wellness360.taskmanager.advice;

import com.wellness360.taskmanager.exception.BadRequestException;
import com.wellness360.taskmanager.exception.InvalidStatusTransitionException;
import com.wellness360.taskmanager.exception.TaskNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
class TestExceptionController {

    @GetMapping("/test-exceptions/not-found")
    void notFound() {
        throw new TaskNotFoundException(UUID.randomUUID());
    }

    @GetMapping("/test-exceptions/bad-request")
    void badRequest() {
        throw new BadRequestException("Invalid input");
    }

    @GetMapping("/test-exceptions/invalid-transition")
    void invalidTransition() {
        throw new InvalidStatusTransitionException("Invalid transition");
    }
}
