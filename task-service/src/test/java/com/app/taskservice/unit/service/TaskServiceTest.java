package com.app.taskservice.unit.service;

import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.entity.task.TaskStatus;
import com.app.taskservice.exception.custom.InvalidTaskStatusException;
import com.app.taskservice.mapper.task.CreateRequestTaskMapper;
import com.app.taskservice.repository.TaskRepository;
import com.app.taskservice.service.TaskService;
import com.app.taskservice.service.impl.TaskServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    private static final Long ID = 1L;
    private static TaskEntity TASK_ENTITY;
    @InjectMocks
    private TaskServiceImpl taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CreateRequestTaskMapper createRequestTaskMapper;

    @BeforeAll
    static void setTaskEntity() {
        TASK_ENTITY = new TaskEntity(
                ID,
                "test-title",
                "test-descr",
                TaskStatus.TO_DO
        );
    }
    @Test
    void createTask_validRequest_shouldSave() {
        CreateTaskRequest createTaskRequest = CreateTaskRequest.builder()
                .title("test-title")
                .description("test-descr")
                .build();

        when(createRequestTaskMapper.toEntity(createTaskRequest))
                .thenReturn(TASK_ENTITY);
        when(taskRepository.save(TASK_ENTITY))
                .thenReturn(TASK_ENTITY);

        TaskEntity actualEntity = taskService.createTask(createTaskRequest);

        assertThat(actualEntity).isEqualTo(TASK_ENTITY);

        verify(createRequestTaskMapper).toEntity(createTaskRequest);
        verify(taskRepository).save(TASK_ENTITY);
    }

    @Test
    void getTaskById_existsId_shouldReturnTask() {
        when(taskRepository.findById(ID))
                .thenReturn(Optional.of(TASK_ENTITY));

        TaskEntity actualEntity = taskService.getTaskById(ID);

        assertThat(actualEntity).isEqualTo(TASK_ENTITY);

        verify(taskRepository).findById(ID);
    }

    @Test
    void getTaskById_nonExistsId_throwEntityNotFound() {
        when(taskRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.deleteTask(ID))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @EnumSource(TaskStatus.class)
    @ParameterizedTest
    void updateTaskStatus_validStatusAndExistsId_shouldChangeStatus(TaskStatus validStatus) {
        TaskEntity entityWithUpdatedStatus = new TaskEntity();

        when(taskRepository.findById(ID))
                .thenReturn(Optional.of(entityWithUpdatedStatus));
        when(taskRepository.save(entityWithUpdatedStatus))
                .thenReturn(entityWithUpdatedStatus);

        TaskEntity actualEntity = taskService.updateTaskStatus(ID, validStatus.name());

        assertThat(actualEntity).isEqualTo(entityWithUpdatedStatus);
        assertThat(actualEntity.getStatus()).isEqualTo(validStatus);

        verify(taskRepository).findById(ID);
        verify(taskRepository).save(entityWithUpdatedStatus);
    }

    @EmptySource
    @ParameterizedTest
    void updateTaskStatus_emptyStatus_throwsInvalidTaskStatusEx(String invalidStatus) {
        TaskEntity entity = new TaskEntity();

        when(taskRepository.findById(ID))
                .thenReturn(Optional.of(entity));


        assertThatThrownBy(() -> taskService.updateTaskStatus(ID, invalidStatus))
                .isInstanceOf(InvalidTaskStatusException.class);

        verify(taskRepository).findById(ID);
    }

    @NullSource
    @ParameterizedTest
    void updateTaskStatus_nullStatus_throwsIllegalArgEx(String invalidStatus) {
        TaskEntity entity = new TaskEntity();

        assertThatThrownBy(() -> taskService.updateTaskStatus(ID, invalidStatus))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateTaskStatus_nonExistsId_throwEntityNotFound() {
        when(taskRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTaskStatus(ID, "dummy"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteTaskById_existsId_shouldDelete() {
        when(taskRepository.findById(ID))
                .thenReturn(Optional.of(TASK_ENTITY));

        doNothing()
                .when(taskRepository).delete(TASK_ENTITY);

        taskService.deleteTask(ID);

        verify(taskRepository).findById(ID);
        verify(taskRepository).delete(TASK_ENTITY);
    }

    @Test
    void deleteTaskById_nonExistsId_throwEntityNotFoundEx() {
        when(taskRepository.findById(ID))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> taskService.deleteTask(ID))
                .isInstanceOf(EntityNotFoundException.class);

        verify(taskRepository).findById(ID);
    }

}
