package com.directors.infrastructure.exception;

import com.directors.infrastructure.exception.api.ExteralApiAuthenticationException;
import com.directors.infrastructure.exception.api.ExternalApiServerException;
import com.directors.infrastructure.exception.api.NotFoundException;
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;
import com.directors.infrastructure.exception.question.QuestionDuplicateException;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.infrastructure.exception.room.CannotCreateRoomException;
import com.directors.infrastructure.exception.room.RoomNotFoundException;
import com.directors.infrastructure.exception.schedule.ClosedScheduleException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingRequest;
import com.directors.infrastructure.exception.schedule.InvalidMeetingTimeException;
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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ResponseEntity<>(new ErrorMessage(bindingResult.getFieldErrors().get(0).getDefaultMessage()),
                HttpStatus.BAD_REQUEST);
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
        return new ErrorMessage("유효하지 않은 토큰입니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpStatus> IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        log.info("IllegalArgumentException occurred.");
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClosedScheduleException.class)
    public ErrorMessage closedScheduleExceptionHandler(ClosedScheduleException ex) {
        log.info(String.format("ClosedScheduleException occurred. time = %s, userId = %s", ex.getStartTime(),
                ex.getUserId()));
        return new ErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidMeetingTimeException.class)
    public ErrorMessage invalidMeetingTimeException(InvalidMeetingTimeException ex) {
        log.info("InvalidMeetingTimeException occurred. userId = {} startTIme = {} ", ex.getUserId(),
                ex.getStartTime());
        return new ErrorMessage(ex.getMessage());
    }

    @ExceptionHandler(InvalidMeetingRequest.class)
    public ResponseEntity<?> invalidMeetingException(InvalidMeetingRequest ex) {
        log.info("{} occurred, userId = {}, startTime = {}", ex.getMessage(), ex.getUserId(),
                ex.getStartTime());
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(HttpClientErrorException.class)
    public ErrorMessage httpClientErrorException(HttpClientErrorException ex) {
        String errorMessage = "HttpClientErrorException occurred. " + ex.getStatusCode();
        if (ex.getStatusCode().equals("412 PRECONDITION_FAILED")) {
            errorMessage += "need to check api request parameter.";
        }

        log.warn(errorMessage);
        return new ErrorMessage("잠시 후 다시 시도해주세요");
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ExteralApiAuthenticationException.class)
    public ErrorMessage exteralApiAuthenticationException(ExteralApiAuthenticationException ex) {
        log.error("ExteralApiAuthenticationException occurred. " + ex.getMessage());
        return new ErrorMessage("잠시 후 다시 시도해주세요");
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(HttpServerErrorException.class)
    public ErrorMessage httpServerErrorException(HttpServerErrorException ex) {
        log.error("HttpServerErrorException occurred. " + ex.getStatusCode());
        return new ErrorMessage("잠시 후 다시 시도해주세요");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorMessage notFoundException(NotFoundException ex) {
        log.info("NotFoundException occurred.");
        return new ErrorMessage(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ExternalApiServerException.class)
    public ErrorMessage externalApiServerException(NotFoundException ex) {
        log.warn("ExternalApiServerException occurred. An exception occurred in the sgis server.");
        return new ErrorMessage("잠시 후 다시 시도해주세요");
    }

    @ExceptionHandler(QuestionNotFoundException.class)
    public ResponseEntity<?> questionNotFoundException(QuestionNotFoundException ex) {
        log.info("QuestionNotFoundException. questionId = {}", ex.getQuestionId());
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
    }

    @ExceptionHandler(InvalidQuestionStatusException.class)
    public ResponseEntity<?> invalidQuestionStatusException(InvalidQuestionStatusException ex) {
        log.info("InvalidQuestionStatusException occurred. questionId = {}, questionStatus = {}", ex.getQuestionId(),
                ex.getQuestionStatus());
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
    }

    @ExceptionHandler(QuestionDuplicateException.class)
    public ResponseEntity<?> questionDuplicateException(QuestionDuplicateException ex) {
        log.info("questionDuplicateException occurred. questionId = {}", ex.getQuestionId());
        return new ResponseEntity<>(new ErrorMessage(ex.getMessage()), ex.getStatusCode());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(CannotCreateRoomException.class)
    public ErrorMessage cannotCreateRoomException(CannotCreateRoomException e) {
        log.info("cannotCreateRoomException occurred. questionId = " + e.questionId);
        return new ErrorMessage(e.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(RoomNotFoundException.class)
    public ErrorMessage roomNotFoundException(RoomNotFoundException e) {
        log.info("RoomNotFoundException occurred. " + "roomId = " + e.getRoomId() +
                ", requestUserId = " + e.getRequestUserId());
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