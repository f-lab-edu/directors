package com.directors.presentation.qeustion.response;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;

import lombok.Builder;

@Builder
public class QuestionResponseDto {
	private Long id;
	private String title;
	private QuestionStatus status;
	private String directorId;
	private String category; // 차후 카테고리 확정되면 enum으로 수정 예정.

	public static QuestionResponseDto from(Question question) {
		return QuestionResponseDto.builder()
			.id(question.getId())
			.title(question.getTitle())
			.status(question.getStatus())
			.directorId(question.getDirectorId())
			.category(question.getCategory())
			.build();
	}
}
