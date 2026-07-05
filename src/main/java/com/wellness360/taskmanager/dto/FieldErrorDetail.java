package com.wellness360.taskmanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Field-level validation error detail")
public class FieldErrorDetail {

    @Schema(description = "Name of the invalid field", example = "title")
    private String field;

    @Schema(description = "Rejected value", example = "")
    private Object rejectedValue;

    @Schema(description = "Validation error message", example = "Title is required and cannot be blank")
    private String message;

    public FieldErrorDetail() {
    }

    public FieldErrorDetail(String field, Object rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    public void setRejectedValue(Object rejectedValue) {
        this.rejectedValue = rejectedValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
