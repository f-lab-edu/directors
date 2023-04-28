package com.directors.domain.feedback.exception;

public class CannotCreateFeedbackException extends RuntimeException {
    public final static String STATUS = "피드백을 만들 수 없는 상태입니다.";
    public final static String AUTH = "피드백 생성에 대한 권한이 없습니다.";

    public final Long questionId;

    public CannotCreateFeedbackException(Long questionId, String message) {
        super(message);
        this.questionId = questionId;
    }
}
