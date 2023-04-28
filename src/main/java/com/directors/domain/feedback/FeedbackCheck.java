package com.directors.domain.feedback;

public enum FeedbackCheck {
    // FeedbackRating이 BEST일 때 항목
    GOOD_MANNER("BS1"), // 매너가 좋아요
    RESPONSIVENESS("BS2"), // 응답이 빨라요
    TIMELINESS_MET("BS3"), // 시간 약속을 잘 지켜요
    PROFESSIONAL("BS4"), // 전문적이에요
    HELPFULNESS("BS6"), // 도움이 되었어요

    // FeedbackRating이 GOOD일 때 항목
    EXPERIENCED("GD1"),
    FRIENDLY("GD2"),
    ATTENTIVE("GD3"),
    ADAPTABLE("GD4"),
    HELPFULNESS_MODERATE("GD5"),

    // FeedbackRating이 BAD일 때 항목
    BAD_MANNER("BD1"), // 매너가 나빠요
    APPOINTMENT_BREAKING("BD2"), // 약속을 지키지 않았어요
    RESPONSIVENESS_SLOW("BD3"), // 응답이 느려요
    TIMELESSNESS_NOT_MET("BD4"), // 시간 약속을 잘 지키지 않았어요.
    UNPROFESSIONAL("BD5"), // 전문적이지 않아요.
    UNHELPFULNESS("BD6");  // 도움이 되지 않았어요.

    private final String value;

    FeedbackCheck(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static FeedbackCheck fromValue(String value) {
        for (FeedbackCheck check : FeedbackCheck.values()) {
            if (check.getValue().equals(value)) {
                return check;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
