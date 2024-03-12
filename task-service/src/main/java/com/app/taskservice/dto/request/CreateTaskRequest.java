package com.app.taskservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTaskRequest (
        @NotNull(message = "Task title must not be null")
        @NotBlank(message = "Task title must not be blank")
        String title,
        String description
){
}
