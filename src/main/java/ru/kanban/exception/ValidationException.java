package ru.kanban.exception;

public class ValidationException extends RuntimeException {
    public ValidationException (String message) {
        super(message);
    }
}
