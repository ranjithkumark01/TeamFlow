package com.teamflow.api;

import java.time.OffsetDateTime;
import java.util.Map;

public record ValidationErrorResponse(
        boolean success,
        String message,
        Map<String, String> errors,
        OffsetDateTime timestamp
) {
    public static ValidationErrorResponse of(String message, Map<String, String> errors) {
        return new ValidationErrorResponse(false, message, errors, OffsetDateTime.now());
    }
}

