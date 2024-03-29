package com.app.authservice.exception;

import com.app.authservice.exception.custom.GenerateTokenException;
import com.app.authservice.exception.custom.InvalidPasswordException;
import com.app.authservice.exception.custom.SignupException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ControllerExHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationEx(MethodArgumentNotValidException ex) {
        return new ErrorResponse(ex.getMessage(), ex);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse handleEntityNotFoundEx(EntityNotFoundException ex) {
        return new ErrorResponse(ex.getMessage(), ex);
    }

    @ExceptionHandler(SignupException.class)
    public ErrorResponse handleSignUpException(SignupException ex) {
        return new ErrorResponse(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(GenerateTokenException.class)
    public ErrorResponse handleGenerateTokenEx(GenerateTokenException ex) {
        return new ErrorResponse(ex.getMessage(), ex);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(InvalidPasswordException.class)
    public ErrorResponse handleInvalidPasswordException(InvalidPasswordException ex) {
        return new ErrorResponse(ex.getMessage(), ex);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception ex) {
        log.error("Unexpected exception: {}", ex.getMessage());
        ex.printStackTrace(); //добавил
        return new ErrorResponse(ex);
    }
}
