package com.directors.infrastructure.exception;

import org.springframework.http.HttpStatus;

public enum ExceptionCode {
	ClosedSchedule("동일한 시각에 예약한 사람이 있습니다", HttpStatus.CONFLICT),
	InvalidMeetingTime("디렉터가 허용한 만남시간이 아닙니다.", HttpStatus.FORBIDDEN);

	private final String message;
	private final HttpStatus status;

	ExceptionCode(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	public String getMessage() {
		return this.message;
	}

	public HttpStatus getStatus() {
		return this.status;
	}

}