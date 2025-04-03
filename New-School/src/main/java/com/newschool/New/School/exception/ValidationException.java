package com.newschool.New.School.exception;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationException extends RuntimeException {

    private final Map<String, String> errors;

    public ValidationException(String message) {
        super(message);
        this.errors = new HashMap<>();
    }

    public ValidationException(String message, Map<String, String> errors) {
        super(message);
        this.errors = errors;
    }

    public void addError(String field, String message) {
        this.errors.put(field, message);
    }

    // Método de utilidad para crear excepciones de validación para Grado
    public static ValidationException createForGrado() {
        return new ValidationException("Error de validación en grado");
    }
}