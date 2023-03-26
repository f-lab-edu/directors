package com.directors.application.question;

import java.time.LocalDateTime;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import com.directors.presentation.qeustion.request.CreateQuestionRequest;

public class QuestionConverter {

	public static Question of(CreateQuestionRequest createRequest, String questionerId, Long scheduledId) {
		return Question.builder()
			.title(createRequest.getTitle())
			.content(createRequest.getContent())
			.status(QuestionStatus.WAITING)
			.questionCheck(false)
			.directorCheck(false)
			.questionerId(questionerId)
			.directorId(createRequest.getDirectorId())
			.category(createRequest.getCategory())
			.scheduledId(scheduledId)
			.startTime(createRequest.getStartTime())
			.createTime(LocalDateTime.now())
			.build();
	}
}
