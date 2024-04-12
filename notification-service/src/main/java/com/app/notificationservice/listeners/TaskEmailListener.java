package com.app.notificationservice.listeners;

import com.app.notificationservice.service.EmailService;
import com.app.rabbit.mail.TaskEmailDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TaskEmailListener {

    private final EmailService emailService;

    @RabbitListener(queues = "${email-exchange.queues.task-report.name}")
    public void processEmailMessage(TaskEmailDetails taskEmailDetails) {
        log.info("Received message: {}", taskEmailDetails);
        emailService.sendTaskReportMessage(taskEmailDetails);
    }
}
