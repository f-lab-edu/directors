package com.directors.domain.question;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
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
	private String scheduledId;
	private LocalDateTime startTime;
}
