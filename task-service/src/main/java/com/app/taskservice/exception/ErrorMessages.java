package com.app.taskservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorMessages {
    ENTITY_NOT_FOUND("Task not found!"),
    INVALID_TASK_STATUS("Invalid task status");

    private final String message;
}
