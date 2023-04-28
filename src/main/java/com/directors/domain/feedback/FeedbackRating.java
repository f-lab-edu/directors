package com.directors.domain.feedback;

public enum FeedbackRating {
    BEST("최고"),
    GOOD("보통"),
    BAD("별로");

    private final String value;

    FeedbackRating(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FeedbackRating fromValue(String value) {
        for (FeedbackRating rating : FeedbackRating.values()) {
            if (rating.getValue().equals(value)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
