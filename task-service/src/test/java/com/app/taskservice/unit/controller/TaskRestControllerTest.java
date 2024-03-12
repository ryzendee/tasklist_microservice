package com.app.taskservice.unit.controller;

import com.app.taskservice.controller.TaskRestController;
import com.app.taskservice.dto.request.CreateTaskRequest;
import com.app.taskservice.dto.response.TaskResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.entity.task.TaskStatus;
import com.app.taskservice.mapper.task.TaskEntityResponseMapper;
import com.app.taskservice.service.TaskService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;


@WebMvcTest(TaskRestController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TaskRestControllerTest {

    private static final Long ID = 1L;
    private static TaskResponse TASK_RESPONSE;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskEntityResponseMapper taskEntityResponseMapper;

    @MockBean
    private TaskService taskService;
    
    @BeforeAll
    static void setUpStatic() {
        TASK_RESPONSE = TaskResponse.builder()
                .id(ID)
                .status(TaskStatus.TO_DO.name())
                .title("test-dto-title")
                .description("test-dto-descr")
                .build();
    }

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void createTask_validRequestDto_statusCreated() {
        CreateTaskRequest requestDto = CreateTaskRequest.builder()
                .title("test-title")
                .description("test-descr")
                .build();

        TaskEntity entity = new TaskEntity();
        when(taskService.createTask(requestDto))
                .thenReturn(entity);
        when(taskEntityResponseMapper.toDto(entity))
                .thenReturn(TASK_RESPONSE);

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .when()
                    .post("/api/v1/tasks")
                .then()
                    .status(HttpStatus.CREATED);

        verify(taskService).createTask(requestDto);
        verify(taskEntityResponseMapper).toDto(entity);
    }

    @NullSource
    @EmptySource
    @ParameterizedTest
    void createTask_invalidRequestDto_statusBadRequest(String invalidTitle) {
        CreateTaskRequest requestDto = CreateTaskRequest.builder()
                .title(invalidTitle)
                .build();

        RestAssuredMockMvc.given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestDto)
                .when()
                    .post("/api/v1/tasks")
                .then()
                    .status(HttpStatus.BAD_REQUEST);
    }

    @EnumSource(TaskStatus.class)
    @ParameterizedTest
    void updateStatus_validStatus_statusOk(TaskStatus validStatus) {
        TaskEntity entity = new TaskEntity();
        when(taskService.updateTaskStatus(ID, validStatus.name()))
                .thenReturn(entity);

        RestAssuredMockMvc.given()
                .when()
                    .put("/api/v1/tasks/{taskId}?status={status}", String.valueOf(ID), validStatus.name())
                .then()
                    .status(HttpStatus.OK);

        verify(taskService).updateTaskStatus(ID, validStatus.name());
    }

    @EmptySource
    @ValueSource(strings = "dummy")
    @ParameterizedTest
    void updateStatus_invalidStatus_statusBadRequest(String invalidStatus) {
        RestAssuredMockMvc.given()
                .when()
                    .put("/api/v1/tasks/{taskId}?status={status}", String.valueOf(ID), invalidStatus)
                .then()
                    .status(HttpStatus.OK);
    }

    @Test
    void deleteTaskById_existsId_statusNoContent() {
        when(taskService.deleteTask(ID))
                .thenReturn(true);

        RestAssuredMockMvc.given()
                .when()
                    .delete("/api/v1/tasks/{taskId}", String.valueOf(ID))
                .then()
                    .status(HttpStatus.NO_CONTENT);

        verify(taskService).deleteTask(ID);
    }
}
