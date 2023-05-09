package com.directors.presentation.question.response;

import java.time.LocalDateTime;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.specialty.SpecialtyProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceivedQuestionResponse {
	private Long id;
	private String title;
	private QuestionStatus status;
	private String questioner;
	private LocalDateTime startTime;
	private SpecialtyProperty category;

	public static ReceivedQuestionResponse from(Question question) {
		return ReceivedQuestionResponse.builder()
			.id(question.getId())
			.status(question.getStatus())
			.title(question.getTitle())
			.category(question.getCategory())
			.questioner(question.getQuestioner().getNickname())
			.startTime(question.getSchedule().getStartTime())
			.build();
	}
}
