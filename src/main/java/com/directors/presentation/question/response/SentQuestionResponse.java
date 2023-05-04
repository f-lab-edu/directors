package com.directors.presentation.question.response;

import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SentQuestionResponse {
    private Long id;
    private String title;
    private QuestionStatus status;
    private String directorId; // 조인연산 필요, DB 붙이게되면 userName으로 수정 필요
    private String category; // 차후 카테고리 확정되면 enum으로 수정 예정.

    public static SentQuestionResponse from(Question question) {
        return SentQuestionResponse.builder()
                .id(question.getId())
                .title(question.getTitle())
                .status(question.getStatus())
                .directorId(question.getDirector().getId())
                .category(question.getCategory())
                .build();
    }
}
