package com.app.taskservice.controller;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.mapper.task.TaskEntityResponseMapper;
import com.app.taskservice.service.TaskService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;
    private final TaskEntityResponseMapper taskEntityResponseMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        TaskEntity task =  taskService.createTask(createTaskRequest);
        return taskEntityResponseMapper.toDto(task);
    }

    @PutMapping("/{taskId}")
    public TaskResponse updateTaskStatusById(@PathVariable Long taskId,
                                             @RequestParam @NotNull String status) {

        TaskEntity task = taskService.updateTaskStatus(taskId, status);
        return taskEntityResponseMapper.toDto(task);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTaskById(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
    }
}
