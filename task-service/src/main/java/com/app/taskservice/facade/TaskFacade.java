package com.app.taskservice.facade;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;

public interface TaskFacade {

    TaskResponse createTask(CreateTaskRequest createTaskRequest);
    TaskResponse updateTaskStatusById(Long taskId, String status);
    TaskResponse updateTaskById(Long taskId, UpdateTaskRequest updateTaskRequest);
    boolean deleteTaskById(Long taskId);
}
