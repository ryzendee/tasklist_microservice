package com.app.rabbit.mail;

public record TaskEmailDetails(
        String email,
        long allTasks,
        long completedTasks,
        long tasksInProcess
) {
}
