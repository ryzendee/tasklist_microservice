package com.app.taskservice.mapper.task;

import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.MapToDto;

public interface TaskEntityResponseMapper extends MapToDto<TaskEntity, TaskResponse> {
    TaskResponse toDto(TaskEntity entity);
}
