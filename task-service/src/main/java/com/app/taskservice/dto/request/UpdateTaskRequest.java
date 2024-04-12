package com.app.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(
        @NotNull(message = "Task title must not be null")
        @NotBlank(message = "Task title must not be blank")
        String title,
        @NotNull(message = "Task status must not be null")
        @NotBlank(message = "Task status must not be blank")
        String status,
        String description
) {
}
