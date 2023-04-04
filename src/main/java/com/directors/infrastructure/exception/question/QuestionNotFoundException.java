package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;

import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class QuestionNotFoundException extends RuntimeException {
	private Long questionId;
	private String message;
	private HttpStatus statusCode;

	public QuestionNotFoundException() {
		super();
	}

	public QuestionNotFoundException(ExceptionCode exceptionCode, Long questionId) {
		super(exceptionCode.getMessage());
		this.questionId = questionId;
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
	}
}
