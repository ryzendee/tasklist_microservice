package com.app.taskservice.service;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;

public interface TaskService {

    TaskEntity getTaskById(Long id);
    TaskEntity createTask(CreateTaskRequest taskRequest);
    TaskEntity updateTaskStatus(Long id, String status);
    boolean deleteTask(Long id);

}
