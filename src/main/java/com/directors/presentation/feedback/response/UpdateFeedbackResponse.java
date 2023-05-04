package com.directors.presentation.feedback.response;

import com.directors.domain.feedback.Feedback;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateFeedbackResponse(
        Long feedbackId,
        String rating,
        String description,
        List<String> checked
) {
    public static UpdateFeedbackResponse from(Feedback feedback) {
        return UpdateFeedbackResponse.builder()
                .feedbackId(feedback.getId())
                .rating(feedback.getFeedbackRating().getValue())
                .description(feedback.getDescription())
                .checked(feedback.getFeedbackCheckValues())
                .build();
    }
}
