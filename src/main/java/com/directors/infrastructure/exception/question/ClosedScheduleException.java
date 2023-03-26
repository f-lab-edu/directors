package com.directors.infrastructure.exception.question;

public class ClosedScheduleException extends RuntimeException {
	public ClosedScheduleException() {
		super();
	}

	public ClosedScheduleException(String message) {
		super(message);
	}

	public ClosedScheduleException(String message, Throwable cause) {
		super(message, cause);
	}
}
