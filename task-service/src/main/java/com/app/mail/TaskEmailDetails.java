package com.app.mail;

public record TaskEmailDetails(
        String email,
        long allTasks,
        long completedTasks,
        long tasksInProcess
) {
}
