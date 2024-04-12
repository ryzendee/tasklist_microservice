package com.app.taskservice.facade;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;

public interface TaskFacade {

    TaskResponse createTask(CreateTaskRequest createTaskRequest);
    TaskResponse updateTaskStatus(Long taskId, String status);
    boolean deleteTaskById(Long taskId);
}
