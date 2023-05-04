package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;

import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class QuestionDuplicateException extends RuntimeException {
	private String message;
	private HttpStatus statusCode;
	private String questionerId;

	public QuestionDuplicateException() {
		super();
	}

	public QuestionDuplicateException(ExceptionCode exceptionCode, String questionerId) {
		super(exceptionCode.getMessage());
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
		this.questionerId = questionerId;
	}
}
