package com.app.notificationservice.enums.email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TemplatePath {

    TASK_REPORT("/email/task-report-view.ftlh"),
    WELCOME_MESSAGE("/email/welcome-message-view.ftlh");


    private final String path;
}
