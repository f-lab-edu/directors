package com.directors.presentation.question.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeclineQuestionRequest {
	@NotBlank
	private String Comment;
}
