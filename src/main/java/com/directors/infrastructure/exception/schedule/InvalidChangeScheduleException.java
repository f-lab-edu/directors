package com.directors.infrastructure.exception.schedule;

import java.time.LocalDateTime;

import com.directors.domain.question.QuestionStatus;

import lombok.Getter;

@Getter
public class InvalidChangeScheduleException extends RuntimeException {
	private String startTime;
	private QuestionStatus status;
	private final static String message = "해당 시간에 요청받은 존재하며 스케쥴의 상태를 변경할 수 없습니다.";

	public InvalidChangeScheduleException(LocalDateTime startTime, QuestionStatus status) {
		super(message);
		this.startTime = startTime.toString();
		this.status = status;
	}
}
