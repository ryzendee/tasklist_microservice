package com.app.taskservice.mapper.impl;

import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.TaskEntityResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskEntityResponseMapperImpl implements TaskEntityResponseMapper {

    public TaskResponse toDto(TaskEntity entity) {
        return TaskResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .status(entity.getStatus().name())
                .build();
    }
}
