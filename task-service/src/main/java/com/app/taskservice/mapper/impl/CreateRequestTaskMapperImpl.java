package com.app.taskservice.mapper.impl;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.CreateRequestTaskMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateRequestTaskMapperImpl implements CreateRequestTaskMapper {

    @Override
    public TaskEntity map(CreateTaskRequest dto) {
        return new TaskEntity(
                dto.userId(), dto.title(), dto.description()
        );
    }
}
