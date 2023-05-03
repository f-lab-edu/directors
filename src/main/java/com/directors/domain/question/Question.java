package com.directors.domain.question;

import java.time.LocalDateTime;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.room.exception.CannotCreateRoomException;
import com.directors.domain.schedule.Schedule;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PUBLIC)
public class Question extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private String content;
	@Enumerated(EnumType.STRING)
	private QuestionStatus status;
	private Boolean directorCheck;
	private Boolean questionCheck;
	private String questionerId;
	private String directorId;
	private String category; // 카테고리 결정되면 enum으로 변경 예정
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", referencedColumnName = "id")
	private Schedule schedule;
	private String comment;

	public void editQuestion(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void changeSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isNewQuestion() {
		return this.id == null;
	}

	public void checkUneditableStatus() {
		if (this.status != QuestionStatus.WAITING) {
			throw new InvalidQuestionStatusException(ExceptionCode.InvalidQuestionStatus, this.id,
				this.status);
		}
	}

	public boolean isChangedTime(LocalDateTime startTime) {
		return this.schedule.equalsStartTime(startTime);
	}

	public void canCreateChatRoom(String directorId) {
		if (this.directorId.equals(directorId)) {
			throw new CannotCreateRoomException(this.id, CannotCreateRoomException.AUTH);
		}

		LocalDateTime now = LocalDateTime.now();
		if (status.equals(QuestionStatus.WAITING) || (this.schedule.getStartTime().getSecond() - now.getSecond() < 0)) {
			throw new CannotCreateRoomException(this.id, CannotCreateRoomException.STATUS);
		}
	}

	public void changeQuestionStatusToChat() {
		this.status = QuestionStatus.CHATTING;
	}

	public void writeDeclineComment(String deniedComment) {
		this.comment = deniedComment;
	}
}
