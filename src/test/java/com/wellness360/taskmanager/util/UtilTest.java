package com.wellness360.taskmanager.util;

import com.wellness360.taskmanager.enums.TaskStatus;
import com.wellness360.taskmanager.exception.BadRequestException;
import com.wellness360.taskmanager.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UtilTest {

    @Test
    @DisplayName("UuidParser parses valid UUID")
    void parseValidUuid() {
        UUID id = UUID.randomUUID();
        assertThat(UuidParser.parse(id.toString())).isEqualTo(id);
    }

    @Test
    @DisplayName("UuidParser rejects invalid UUID")
    void parseInvalidUuid() {
        assertThatThrownBy(() -> UuidParser.parse("not-valid"))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("StatusTransitionValidator allows valid transitions")
    void validTransitions() {
        assertThatCode(() -> StatusTransitionValidator.validateTransition(
                TaskStatus.PENDING, TaskStatus.IN_PROGRESS)).doesNotThrowAnyException();
        assertThatCode(() -> StatusTransitionValidator.validateTransition(
                TaskStatus.IN_PROGRESS, TaskStatus.COMPLETED)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("StatusTransitionValidator rejects invalid transitions")
    void invalidTransition() {
        assertThatThrownBy(() -> StatusTransitionValidator.validateTransition(
                TaskStatus.COMPLETED, TaskStatus.PENDING))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    @DisplayName("StatusTransitionValidator allows same status")
    void sameStatusIsAllowed() {
        assertThatCode(() -> StatusTransitionValidator.validateTransition(
                TaskStatus.PENDING, TaskStatus.PENDING)).doesNotThrowAnyException();
    }
}
