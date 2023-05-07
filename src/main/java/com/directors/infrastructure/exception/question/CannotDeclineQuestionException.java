package com.directors.infrastructure.exception.question;

import lombok.Getter;

@Getter
public class CannotDeclineQuestionException extends RuntimeException {
	private final static String message = "질문을 거절할 수 있는 권한이 없습니다.";
	public final String questionId;

	public CannotDeclineQuestionException(String questionId) {
		super(message);
		this.questionId = questionId;
	}
}
