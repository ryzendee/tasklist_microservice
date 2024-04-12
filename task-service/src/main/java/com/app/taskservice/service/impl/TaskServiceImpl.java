package com.app.taskservice.service.impl;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.entity.task.TaskStatus;
import com.app.taskservice.exception.ErrorMessages;
import com.app.taskservice.exception.custom.InvalidTaskStatusException;
import com.app.taskservice.mapper.CreateRequestTaskMapper;
import com.app.taskservice.repository.TaskRepository;
import com.app.taskservice.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
    public TaskEntity getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.ENTITY_NOT_FOUND.getMessage()));
    }

    @Transactional
    @Override
    public TaskEntity updateTaskStatus(Long taskId, String status) throws EntityNotFoundException {
        if (status == null) {
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
    public boolean deleteTask(Long taskId) throws EntityNotFoundException {
        TaskEntity task = getTaskById(taskId);
        taskRepository.delete(task);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TaskEntity> getTasksPageByUserId(Long userId, int page, int pageSize) {
        return null;
    }
}
