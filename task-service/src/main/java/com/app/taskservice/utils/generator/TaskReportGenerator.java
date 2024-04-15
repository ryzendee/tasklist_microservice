package com.app.taskservice.utils.generator;

import com.app.rabbit.mail.TaskEmailDetails;
import com.app.taskservice.dto.response.AuthUserResponse;

public interface TaskReportGenerator {

    TaskEmailDetails generateTaskReportForUser(AuthUserResponse user, int pageSize);
}
