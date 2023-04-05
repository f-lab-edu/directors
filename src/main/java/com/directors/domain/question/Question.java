package com.directors.domain.question;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(access = AccessLevel.PUBLIC)
public class Question {
	private Long id;
	private LocalDateTime createTime;
	private String title;
	private String content;
	private QuestionStatus status;
	private Boolean directorCheck;
	private Boolean questionCheck;
	private String questionerId;
	private String directorId;
	private String category; // 카테고리 결정되면 enum으로 변경 예정
	private Long scheduledId;
	private LocalDateTime startTime;

	public static Question of(String title, String content, String directorId, String category, LocalDateTime startTime,
		String questionerId, Long scheduledId) {
		return Question.builder()
			.title(title)
			.content(content)
			.status(QuestionStatus.WAITING)
			.questionCheck(false)
			.directorCheck(false)
			.questionerId(questionerId)
			.directorId(directorId)
			.category(category)
			.scheduledId(scheduledId)
			.startTime(startTime)
			.createTime(LocalDateTime.now())
			.build();
	}

	public void changeTitleAndContent(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void changeStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNewQuestion() {
		return this.id == null;
	}
}
