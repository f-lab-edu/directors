package com.directors.domain.question;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.feedback.exception.CannotCreateFeedbackException;
import com.directors.domain.room.exception.CannotCreateRoomException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.user.User;
import com.directors.infrastructure.exception.ExceptionCode;
import com.directors.infrastructure.exception.question.InvalidQuestionStatusException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questioner_id", referencedColumnName = "id")
    private User questioner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id", referencedColumnName = "id")
    private User director;

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
        if (this.director.getId().equals(directorId)) {
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

    public void changeQuestionStatusToComplete() {
        this.status = QuestionStatus.COMPLETE;
    }

    public void canCreateFeedback(String questionerId) {
        if (!this.questioner.getId().equals(questionerId)) {
            throw new CannotCreateFeedbackException(this.id, CannotCreateFeedbackException.AUTH);
        }
        if (!this.status.equals(QuestionStatus.COMPLETE)) {
            throw new CannotCreateFeedbackException(this.id, CannotCreateFeedbackException.STATUS);
        }
    }

    public void writeDeclineComment(String deniedComment) {
        this.comment = deniedComment;
    }
}
