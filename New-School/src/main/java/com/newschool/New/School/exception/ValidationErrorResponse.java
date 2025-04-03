package com.newschool.New.School.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse extends ErrorResponse {
    private Map<String, String> errors;

    public ValidationErrorResponse(int status, String message, LocalDateTime timestamp, String path, Map<String, String> errors) {
        super(status, message, timestamp, path);
        this.errors = errors;
    }
}