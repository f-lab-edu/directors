package com.directors.infrastructure.exception.schedule;

import lombok.Getter;

@Getter
public class InvalidMeetingTimeException extends RuntimeException {
	private String userId;

	public InvalidMeetingTimeException() {
	}

	public InvalidMeetingTimeException(String userId) {
		super("디렉터가 허용한 만남시간이 아닙니다.");
		this.userId = userId;
	}

}
