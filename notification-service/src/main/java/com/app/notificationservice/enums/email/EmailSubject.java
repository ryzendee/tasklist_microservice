package com.app.notificationservice.enums.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {

    WELCOME_MESSAGE("Welcome to the Task List Application!");

    private final String subjectName;


}
