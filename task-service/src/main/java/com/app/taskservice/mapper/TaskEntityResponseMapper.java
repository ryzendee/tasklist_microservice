package com.app.taskservice.mapper;

import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;

public interface TaskEntityResponseMapper {
    TaskResponse toDto(TaskEntity entity);
}
