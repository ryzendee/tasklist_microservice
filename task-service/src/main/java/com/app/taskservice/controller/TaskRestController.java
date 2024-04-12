package com.app.taskservice.controller;

import com.app.taskservice.facade.TaskFacade;
import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskFacade taskFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        return taskFacade.createTask(createTaskRequest);
    }

    @PatchMapping("/{taskId}")
    public TaskResponse updateTaskStatusById(@PathVariable @Min(value = 0) Long taskId,
                                             @RequestParam @NotBlank String status) {

        return taskFacade.updateTaskStatus(taskId, status);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTaskById(@PathVariable @Min(value = 0) Long taskId) {
        return taskFacade.deleteTaskById(taskId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }
}
