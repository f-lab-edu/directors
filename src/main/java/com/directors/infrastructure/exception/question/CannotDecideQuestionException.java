package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;

import com.directors.infrastructure.exception.ExceptionCode;

import lombok.Getter;

@Getter
public class CannotDecideQuestionException extends RuntimeException {
	private String message;
	private HttpStatus statusCode;
	public final String questionId;

	public CannotDecideQuestionException(ExceptionCode exceptionCode, String questionId) {
		super(exceptionCode.getMessage());
		this.message = exceptionCode.getMessage();
		this.statusCode = exceptionCode.getStatus();
		this.questionId = questionId;
	}
}
