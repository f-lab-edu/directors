package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;

import com.directors.domain.question.QuestionStatus;
import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class InvalidQuestionStatusException extends RuntimeException {
	private String message;
	private HttpStatus statusCode;
	private Long questionId;
	private QuestionStatus questionStatus;

	public InvalidQuestionStatusException() {
		super();
	}

	public InvalidQuestionStatusException(ExceptionCode exceptionCode, Long questionId, QuestionStatus questionStatus) {
		super(exceptionCode.getMessage());
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
		this.questionId = questionId;
		this.questionStatus = questionStatus;
	}
}
