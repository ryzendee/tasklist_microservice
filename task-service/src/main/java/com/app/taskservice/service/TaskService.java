package com.app.taskservice.service;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import org.springframework.data.domain.Page;

public interface TaskService {

    Page<TaskEntity> getTasksPageByUserId(Long userId, int page, int pageSize);
    TaskEntity getTaskById(Long id);
    TaskEntity createTask(CreateTaskRequest taskRequest);
    TaskEntity updateTaskStatusById(Long id, String status);
    TaskEntity updateTaskById(Long id, UpdateTaskRequest updateTaskRequest);
    boolean deleteTask(Long id);

}
