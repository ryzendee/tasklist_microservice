package com.app.taskservice.dto.response;

import lombok.Builder;

@Builder
public record TaskResponse (
        Long id,
        Long userId,
        String title,
        String description,
        String status
) {
}
