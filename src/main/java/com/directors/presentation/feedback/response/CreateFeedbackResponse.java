package com.directors.presentation.feedback.response;

import com.directors.domain.feedback.Feedback;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateFeedbackResponse(
        Long id,
        String rating,
        List<String> feedbackCheckedList,
        String description,
        Long questionId,
        String directorId,
        String questionerId
) {
    public static CreateFeedbackResponse from(Feedback feedback) {
        return CreateFeedbackResponse.builder()
                .id(feedback.getId())
                .rating(feedback.getFeedbackRating().getValue())
                .feedbackCheckedList(feedback.getFeedbackCheckValues())
                .description(feedback.getDescription())
                .questionId(feedback.getQuestion().getId())
                .directorId(feedback.getDirector().getId())
                .questionerId(feedback.getQuestioner().getId())
                .build();
    }
}
