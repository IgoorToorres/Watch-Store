package com.watch.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}
