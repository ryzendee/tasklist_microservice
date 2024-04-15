package com.app.taskservice.service.impl;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.request.UpdateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.entity.task.TaskStatus;
import com.app.taskservice.exception.ErrorMessages;
import com.app.taskservice.exception.custom.InvalidTaskStatusException;
import com.app.taskservice.exception.custom.TaskNotFoundException;
import com.app.taskservice.mapper.CreateRequestTaskMapper;
import com.app.taskservice.repository.TaskRepository;
import com.app.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final CreateRequestTaskMapper taskMapper;

    @Transactional
    @Override
    public TaskEntity createTask(CreateTaskRequest taskRequest) {
        TaskEntity taskEntity = taskMapper.map(taskRequest);
        taskEntity.setStatus(TaskStatus.TO_DO);
        return taskRepository.save(taskEntity);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskEntity> getTasksPageByUserId(Long userId, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return taskRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    @Override
    public TaskEntity updateTaskStatusById(Long taskId, String status) throws TaskNotFoundException {
        if (StringUtils.isBlank(status)) {
            throw new IllegalArgumentException(ErrorMessages.INVALID_TASK_STATUS.getMessage() + " : " + status);
        }

        TaskEntity task = getTaskById(taskId);

        try {
            task.setStatus(TaskStatus.valueOf(status));
        } catch (NullPointerException | IllegalArgumentException ex) {
            throw new InvalidTaskStatusException(ErrorMessages.INVALID_TASK_STATUS.getMessage() + " : " + status);
        }

        return taskRepository.save(task);
    }

    @Transactional
    @Override
    public TaskEntity updateTaskById(Long id, UpdateTaskRequest updateTaskRequest) throws TaskNotFoundException {
        TaskEntity taskEntity = getTaskById(id);

        taskEntity.setTitle(updateTaskRequest.title());
        taskEntity.setStatus(TaskStatus.valueOf(updateTaskRequest.status()));
        taskEntity.setDescription(updateTaskRequest.description());

        return taskRepository.save(taskEntity);
    }

    @Transactional
    @Override
    public boolean deleteTask(Long taskId) throws TaskNotFoundException {
        TaskEntity task = getTaskById(taskId);
        taskRepository.delete(task);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

}
