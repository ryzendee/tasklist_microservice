package com.app.taskservice.utils.generator;

import com.app.rabbit.mail.TaskEmailDetails;
import com.app.taskservice.dto.response.AuthUserResponse;
import com.app.taskservice.entity.task.TaskEntity;
import com.app.taskservice.entity.task.TaskStatus;
import com.app.taskservice.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskReportGeneratorImpl implements TaskReportGenerator {

    private final TaskService taskService;

    @Override
    public TaskEmailDetails generateTaskReportForUser(AuthUserResponse authUserResponse, int pageSize) {
        int page = 0;
        long totalTasks = 0;
        long completedTasks = 0;
        long tasksInProcess = 0;
        boolean continueExecuting = true;

        while (continueExecuting) {
            Page<TaskEntity> userTasks = taskService.getTasksPageByUserId(authUserResponse.id(), page, pageSize);
            page++;

            if (userTasks.isEmpty()) {
                continueExecuting = false;
            }

            totalTasks += userTasks.getTotalElements();
            completedTasks += userTasks.stream()
                    .filter(taskEntity -> taskEntity.getStatus().equals(TaskStatus.COMPLETED))
                    .count();
        }
        tasksInProcess -= totalTasks - completedTasks;

        return new TaskEmailDetails(authUserResponse.email(), totalTasks, completedTasks, tasksInProcess);
    }

}
