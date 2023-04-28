package com.directors.domain.feedback.exception;

import lombok.Getter;

@Getter
public class FeedbackNotFoundException extends RuntimeException {
    private Long feedbackId;
    private String requestUserId;
    private final static String message = "존재하지 않는 피드백입니다.";

    public FeedbackNotFoundException(Long feedbackId, String requestUserId) {
        super(message);
        this.feedbackId = feedbackId;
        this.requestUserId = requestUserId;
    }
}
