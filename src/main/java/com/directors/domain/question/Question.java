package com.directors.domain.question;

import com.directors.domain.feedback.exception.CannotCreateFeedbackException;
import com.directors.domain.room.exception.CannotCreateRoomException;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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

    public void editQuestion(String title, String content, LocalDateTime startTime) {
        this.title = title;
        this.content = content;
        this.startTime = startTime;
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
        return this.startTime.equals(startTime);
    }


    public void canCreateChatRoom(String directorId) {
        if (this.directorId.equals(directorId)) {
            throw new CannotCreateRoomException(this.id, CannotCreateRoomException.AUTH);
        }

        LocalDateTime now = LocalDateTime.now();
        if (status.equals(QuestionStatus.WAITING) || (this.startTime.getSecond() - now.getSecond() < 0)) {
            throw new CannotCreateRoomException(this.id, CannotCreateRoomException.STATUS);
        }
    }

    public void changeQuestionStatusToChat() {
        this.status = QuestionStatus.CHATTING;
    }

    public void canCreateFeedback(String questionerId) {
        if (this.questionerId.equals(questionerId)) {
            throw new CannotCreateFeedbackException(this.id, CannotCreateFeedbackException.AUTH);
        }
        if (this.status.equals(QuestionStatus.COMPLETE)) {
            throw new CannotCreateFeedbackException(this.id, CannotCreateFeedbackException.STATUS);
        }
    }
}
