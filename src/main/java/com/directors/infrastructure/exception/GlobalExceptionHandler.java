package com.directors.infrastructure.exception;

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

import com.directors.infrastructure.exception.schedule.ClosedScheduleException;
import com.directors.infrastructure.exception.schedule.InvalidMeetingTimeException;
import com.directors.infrastructure.exception.user.DuplicateIdException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

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
	public ErrorMessage duplicateIdExceptionHandler(DuplicateIdException ex) {
		log.info("DuplicateIdException occurred. duplicatedId: " + ex.duplicatedId);
		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(ClosedScheduleException.class)
	public ErrorMessage closedScheduleExceptionHandler(ClosedScheduleException ex) {
		log.info(String.format("ClosedScheduleException occurred. time = %s, userId = %s", ex.getStartTime(),
			ex.getUserId()));
		return new ErrorMessage(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(InvalidMeetingTimeException.class)
	public ErrorMessage invalidMeetingTimeException(InvalidMeetingTimeException ex) {
		log.info(String.format("InvalidMeetingTimeException occurred. userId = %s", ex.getUserId()));
		return new ErrorMessage(ex.getMessage());
	}

	@Getter
	private class ErrorMessage {
		private final String message;

		ErrorMessage(String message) {
			this.message = message;
		}
	}
}

