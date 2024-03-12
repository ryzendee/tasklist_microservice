package com.app.taskservice.mapper.task.impl;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.task.CreateRequestTaskMapper;
import org.springframework.stereotype.Component;

@Component
public class CreateRequestTaskMapperImpl implements CreateRequestTaskMapper {

    @Override
    public TaskEntity toEntity(CreateTaskRequest dto) {
        return new TaskEntity(
                dto.title(), dto.description()
        );
    }
}
