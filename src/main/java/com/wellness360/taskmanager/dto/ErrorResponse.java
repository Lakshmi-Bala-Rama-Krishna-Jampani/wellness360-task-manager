package com.wellness360.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Standardized error response")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2026-07-04T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "HTTP status reason phrase", example = "Not Found")
    private String error;

    @Schema(description = "Human-readable error message", example = "Task not found with id: ...")
    private String message;

    @Schema(description = "Request path that caused the error", example = "/tasks/invalid-id")
    private String path;

    @Schema(description = "Distributed tracing identifier", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
    private String traceId;

    @Schema(description = "Field-level validation errors")
    private List<FieldErrorDetail> fieldErrors;

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public List<FieldErrorDetail> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorDetail> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
