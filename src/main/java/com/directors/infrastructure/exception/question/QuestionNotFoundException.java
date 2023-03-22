package com.directors.infrastructure.exception.question;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class QuestionNotFoundException extends RuntimeException {
	public QuestionNotFoundException() {
		super();
	}

	public QuestionNotFoundException(String message) {
		super(message);
	}
}
