package com.directors.presentation.feedback.response;

import com.directors.domain.feedback.Feedback;
import lombok.Builder;

import java.util.List;

@Builder
public record GetByFeedbackIdResponse(
        Long feedbackId,
        String rating,
        List<String> feedbackCheckedList,
        String description
) {

    public static GetByFeedbackIdResponse of(Feedback feedback) {
        return GetByFeedbackIdResponse.builder()
                .feedbackId(feedback.getId())
                .rating(feedback.getFeedbackRating().getValue())
                .feedbackCheckedList(feedback.getFeedbackCheckValues())
                .description(feedback.getDescription())
                .build();
    }
}
