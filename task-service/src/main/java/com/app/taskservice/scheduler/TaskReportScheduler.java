package com.app.taskservice.scheduler;

import com.app.rabbit.mail.TaskEmailDetails;
import com.app.taskservice.dto.response.AuthUserResponse;
import com.app.taskservice.service.AuthUserService;
import com.app.taskservice.utils.generator.TaskReportGenerator;
import com.app.taskservice.utils.sender.MailQueueSender;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class TaskReportScheduler {

    private static final String SCHEDULE_TIME = "0 10 16 * * *";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_OPERATION_ATTEMPTS = 3;


    private final AuthUserService authUserService;
    private final TaskReportGenerator taskReportGenerator;
    private final MailQueueSender mailQueueSender;


    @Scheduled(cron = SCHEDULE_TIME)
    public void execute() {
        log.info("Task report scheduler starts working");

        int page = 0;
        int operationCounter = 0;
        boolean continueExecuting = true;

        while (continueExecuting) {

            try {
                Page<AuthUserResponse> userResponsePage = authUserService.sendRequestToGetUsers(page, DEFAULT_PAGE_SIZE);
                page++;

                if (userResponsePage.isEmpty()) {
                    continueExecuting = false;
                }

                for (AuthUserResponse user : userResponsePage) {
                    TaskEmailDetails taskReport = taskReportGenerator.generateTaskReportForUser(user, DEFAULT_PAGE_SIZE);

                    if (taskReport.allTasks() <= 0) {
                        log.info("User has not enough tasks for notification: {}", taskReport);
                        continue;
                    }

                    mailQueueSender.send(taskReport);
                    log.info("Task report was sent: {}", taskReport);
                }

            } catch (FeignException ex) {
                log.error("Failed to execute task report scheduler, attempts: {}", operationCounter, ex);
                continueExecuting = operationCounter < MAX_OPERATION_ATTEMPTS;
                operationCounter++;
            }
        }

        log.info("Task report scheduler ends working");
    }
}
