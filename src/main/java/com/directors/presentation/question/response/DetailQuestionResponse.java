package com.directors.presentation.question.response;

import java.time.LocalDateTime;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.specialty.SpecialtyProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DetailQuestionResponse {
	private String title;
	private String content;
	private SpecialtyProperty category;
	private QuestionStatus status;
	private String questionerName;
	private String directorName;
	private Boolean directorCheck;
	private Boolean questionerCheck;
	private LocalDateTime startTime;
	private String comment;

	public static DetailQuestionResponse from(Question question) {
		return DetailQuestionResponse.builder()
			.title(question.getTitle())
			.content(question.getContent())
			.category(question.getCategory())
			.status(question.getStatus())
			.questionerName(question.getQuestioner().getNickname())
			.directorName(question.getDirector().getNickname())
			.directorCheck(question.getDirectorCheck())
			.questionerCheck(question.getQuestionCheck())
			.startTime(question.getSchedule().getStartTime())
			.comment(question.getComment())
			.build();
	}
}
