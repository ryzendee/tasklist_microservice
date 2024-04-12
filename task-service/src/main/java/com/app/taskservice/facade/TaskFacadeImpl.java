package com.app.taskservice.facade;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.TaskEntityResponseMapper;
import com.app.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskFacadeImpl implements TaskFacade {

    private final TaskService taskService;
    private final TaskEntityResponseMapper taskEntityResponseMapper;
    @Override
    public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
        TaskEntity task =  taskService.createTask(createTaskRequest);
        return taskEntityResponseMapper.toDto(task);
    }

    @Override
    public TaskResponse updateTaskStatusById(Long taskId, String status) {
        TaskEntity task = taskService.updateTaskStatusById(taskId, status);
        return taskEntityResponseMapper.toDto(task);
    }

    @Override
    public TaskResponse updateTaskById(Long taskId, UpdateTaskRequest updateTaskRequest) {
        TaskEntity task = taskService.updateTaskById(taskId, updateTaskRequest);
        return taskEntityResponseMapper.toDto(task);
    }

    @Override
    public boolean deleteTaskById(Long taskId) {
        return taskService.deleteTask(taskId);
    }
}
