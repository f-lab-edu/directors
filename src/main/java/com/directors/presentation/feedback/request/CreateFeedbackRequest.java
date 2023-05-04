package com.directors.presentation.feedback.request;

import com.directors.domain.feedback.FeedbackCheck;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record CreateFeedbackRequest(
        Long questionId,
        String rating,
        String description,
        List<String> checkedList
) {
    public List<FeedbackCheck> toFeedbackCheckList() {
        return checkedList.stream()
                .map(s -> FeedbackCheck.fromValue(s))
                .collect(Collectors.toList());
    }
}
