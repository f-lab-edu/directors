package com.directors.infrastructure.exception;

import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import com.directors.infrastructure.exception.user.DuplicateIdException;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import io.jsonwebtoken.JwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 상속한 ResponseEntityExceptionHandler 클래스에 handleMethodArgumentNotValid 메소드가 있기 때문에
     * 이를 오버라이드하여 구현했습니다.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ResponseEntity<>(new ErrorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateIdException.class)
    public ErrorMessage DuplicateIdExceptionHandler(DuplicateIdException e) {
        log.info("DuplicateIdException occurred. duplicatedId: " + e.duplicatedId);
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchUserException.class)
    public ErrorMessage NosuchUserExceptionHandler(NoSuchUserException e) {
        log.info("NosuchUserException occurred. requested userId: " + e.requestedUserId);
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailedException.class)
    public ErrorMessage AuthenticationFailedExceptionHandler(AuthenticationFailedException e) {
        log.info("AuthenticationFailedException occurred. requested userId:" + e.requestedUserId);
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(JwtException.class)
    public ErrorMessage JwtExceptionHandler(JwtException e) {
        log.info("JwtException occurred.");
        return new ErrorMessage(e.getMessage());
    }
}

@Getter
class ErrorMessage {
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}