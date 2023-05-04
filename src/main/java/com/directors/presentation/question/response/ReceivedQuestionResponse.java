package com.directors.presentation.question.response;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReceivedQuestionResponse {
    private Long id;
    private String title;
    private QuestionStatus status;
    private String questionerId; // userName으로 수정 예정
    private LocalDateTime startTime;
    private String category; // 차후 카테고리 확정되면 enum으로 수정 예정.

    public static ReceivedQuestionResponse from(Question question) {
        return ReceivedQuestionResponse.builder()
                .id(question.getId())
                .status(question.getStatus())
                .title(question.getTitle())
                .category(question.getCategory())
                .questionerId(question.getQuestioner().getId())
                .startTime(question.getSchedule().getStartTime())
                .build();
    }
}
