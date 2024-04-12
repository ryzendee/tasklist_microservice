package com.app.taskservice.controller;

import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.facade.TaskFacade;
import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskRestController {

    private static final String DEFAULT_PAGE = "0";
    private static final String DEFAULT_PAGE_SIZE = "30";
    private final TaskFacade taskFacade;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(@Valid @RequestBody CreateTaskRequest createTaskRequest) {
        return taskFacade.createTask(createTaskRequest);
    }

    @GetMapping("/{userId}")
    public Page<TaskResponse> getTasksPageByUserId(@PathVariable @Min(value = 0) Long userId,
                                                   @RequestParam(name = "page", defaultValue = DEFAULT_PAGE) int page,
                                                   @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        return taskFacade.getTasksPageByUserId(userId, page, pageSize);
    }

    @PatchMapping("/{taskId}")
    public TaskResponse updateTaskStatusById(@PathVariable @Min(value = 0) Long taskId,
                                             @RequestParam @NotBlank String status) {

        return taskFacade.updateTaskStatusById(taskId, status);
    }

    @PutMapping("/{taskId}")
    public TaskResponse updateTaskById(@PathVariable @Min(value = 0) Long taskId,
                                       @RequestBody UpdateTaskRequest updateTaskRequest) {
        return taskFacade.updateTaskById(taskId, updateTaskRequest);
    }

    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTaskById(@PathVariable @Min(value = 0) Long taskId) {
        return taskFacade.deleteTaskById(taskId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().build();
    }
}
