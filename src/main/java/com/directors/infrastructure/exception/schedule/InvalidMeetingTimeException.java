package com.directors.infrastructure.exception.schedule;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class InvalidMeetingTimeException extends RuntimeException {
	private String userId;
	private String startTime;

	public InvalidMeetingTimeException() {
	}

	public InvalidMeetingTimeException(LocalDateTime startTime, String userId) {
		super("디렉터가 허용한 만남시간이 아닙니다.");
		this.userId = userId;
		this.startTime = startTime.toString();
	}

}
