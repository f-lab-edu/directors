package com.directors.presentation.question.response;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.specialty.SpecialtyProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentQuestionResponse {
	private Long id;
	private String title;
	private QuestionStatus status;
	private String director;
	private SpecialtyProperty category;

	public static SentQuestionResponse from(Question question) {
		return SentQuestionResponse.builder()
			.id(question.getId())
			.title(question.getTitle())
			.status(question.getStatus())
			.director(question.getDirector().getNickname())
			.category(question.getCategory())
			.build();
	}
}
