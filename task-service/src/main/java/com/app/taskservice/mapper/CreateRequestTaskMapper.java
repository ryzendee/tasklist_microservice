package com.app.taskservice.mapper;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;

public interface CreateRequestTaskMapper {

    TaskEntity map(CreateTaskRequest createTaskRequest);
}
