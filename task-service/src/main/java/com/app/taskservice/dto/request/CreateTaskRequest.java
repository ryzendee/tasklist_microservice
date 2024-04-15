package com.app.taskservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateTaskRequest (

        @NotNull(message = "User id must not be null")
        @Min(value = 0, message = "User id must be positive value")
        Long userId,
        @NotNull(message = "Task title must not be null")
        @NotBlank(message = "Task title must not be blank")
        String title,
        String description
){
}
