package com.directors.infrastructure.exception.schedule;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class InvalidMeetingTimeException extends RuntimeException {
	private String userId;
	private String startTime;
	private String message;
	private HttpStatus statusCode;

	public InvalidMeetingTimeException() {
	}

	public InvalidMeetingTimeException(ExceptionCode errorCode, LocalDateTime startTime, String userId) {
		super(errorCode.getMessage());
		this.userId = userId;
		this.startTime = startTime.toString();
		this.message = errorCode.getMessage();
		this.statusCode = errorCode.getStatus();
	}

}
