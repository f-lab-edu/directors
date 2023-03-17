package com.directors.infrastructure.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExeptionHandler extends ResponseEntityExceptionHandler {

    /**
     * 상속한 ResponseEntityExceptionHandler 클래스에 handleMethodArgumentNotValid 메소드가 있기 때문에
     * 이를 오버라이드하여 구현했습니다.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ResponseEntity<>(new ErrorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage()), HttpStatus.BAD_REQUEST);
    }
}

@Getter
class ErrorMessage {
    private final String message;

    ErrorMessage(String message) {
        this.message = message;
    }
}