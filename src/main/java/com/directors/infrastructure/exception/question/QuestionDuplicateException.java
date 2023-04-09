package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;

import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class QuestionDuplicateException extends RuntimeException {
	private String message;
	private HttpStatus statusCode;
	private Long questionId;

	public QuestionDuplicateException() {
		super();
	}

	public QuestionDuplicateException(ExceptionCode exceptionCode, Long questionId) {
		super(exceptionCode.getMessage());
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
		this.questionId = questionId;
	}
}
