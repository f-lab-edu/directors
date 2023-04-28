package com.directors.presentation.feedback.response;

import java.util.List;

public record GetByFeedbackIdResponse(
        Long feedbackId,
        String rating,
        List<String> feedbackCheckedList,
        String description
) {
}
