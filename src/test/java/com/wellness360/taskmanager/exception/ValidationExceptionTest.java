package com.wellness360.taskmanager.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ValidationExceptionTest {

    @Test
    @DisplayName("Should store single validation message")
    void singleMessage() {
        ValidationException exception = new ValidationException("Invalid field");

        assertThat(exception.getMessage()).isEqualTo("Invalid field");
        assertThat(exception.getErrors()).containsExactly("Invalid field");
    }

    @Test
    @DisplayName("Should store multiple validation messages")
    void multipleMessages() {
        ValidationException exception = new ValidationException(List.of("Error 1", "Error 2"));

        assertThat(exception.getMessage()).isEqualTo("Error 1; Error 2");
        assertThat(exception.getErrors()).containsExactly("Error 1", "Error 2");
    }
}
