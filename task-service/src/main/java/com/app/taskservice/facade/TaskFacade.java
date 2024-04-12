package com.app.taskservice.facade;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import org.springframework.data.domain.Page;

public interface TaskFacade {

    TaskResponse createTask(CreateTaskRequest createTaskRequest);
    Page<TaskResponse> getTasksPageByUserId(Long userId, int page, int pageSize);
    TaskResponse updateTaskStatusById(Long taskId, String status);
    TaskResponse updateTaskById(Long taskId, UpdateTaskRequest updateTaskRequest);
    boolean deleteTaskById(Long taskId);
}
