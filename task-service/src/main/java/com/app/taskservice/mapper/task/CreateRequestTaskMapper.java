package com.app.taskservice.mapper.task;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.MapToDto;
import com.app.taskservice.mapper.MapToEntity;

public interface CreateRequestTaskMapper extends MapToEntity<TaskEntity, CreateTaskRequest> {
}
