package com.app.taskservice.exception;

import com.app.taskservice.exception.custom.InvalidTaskStatusException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExHandler {

    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleValidationEx(ValidationException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundEx(EntityNotFoundException ex) {
        return new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
    }
    @ExceptionHandler(InvalidTaskStatusException.class)
    public ErrorResponse handleInvalidTaskStatusException(InvalidTaskStatusException ex) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), ex);
    }

    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        log.error("Unexpected exception: {}", ex.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }
}
