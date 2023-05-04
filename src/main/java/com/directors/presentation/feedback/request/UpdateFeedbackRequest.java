package com.directors.presentation.feedback.request;

import com.directors.domain.feedback.FeedbackCheck;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UpdateFeedbackRequest(
        Long feedbackId,
        String rating,
        String description,
        List<String> checked
) {
    public List<FeedbackCheck> toFeedbackCheckList() {
        return checked.stream()
                .map(s -> FeedbackCheck.fromValue(s))
                .collect(Collectors.toList());
    }
}
