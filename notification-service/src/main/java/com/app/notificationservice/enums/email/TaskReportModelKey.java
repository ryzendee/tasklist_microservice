package com.app.notificationservice.enums.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskReportModelKey {

    TOTAL_TASKS("totalTasks"),
    COMPLETED_TASKS("completedTasks"),
    TASKS_IN_PROCESS("tasksInProcess");

    private final String key;

}
