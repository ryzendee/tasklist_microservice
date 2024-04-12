package com.app.taskservice.utils.generator;

import com.app.mail.TaskEmailDetails;
import com.app.taskservice.dto.response.UserResponse;

public interface TaskReportGenerator {

    TaskEmailDetails generateTaskReportForUser(UserResponse user, int pageSize);
}
