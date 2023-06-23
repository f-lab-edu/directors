package com.directors.domain.specialty;

public enum SpecialtyProperty {
    // 직무 관련
    PROGRAMMING("프로그래밍"),
    DESIGN("디자인"),
    HEALTH("헬스"),
    ENGINEERING("엔지니어링"),
    EDUCATION("교육"),
    HR("인적자원"),
    BS("경영지원"),
    CONSTRUCTION("건설"),
    SALES("영업"),
    PRODUCTION("생산"),
    MARKETING("마케팅"),
    LEGAL("법무"),
    MEDIA("미디어"),
    HEALTHCARE("의료"),
    SERVICE("서비스"),
    FINANCE("금융"),
    LOGISTICS("물류"),
    TRADE("무역"),
    PROFESSIONAL("전문직"),
    // 일반
    LIFESTYLE("생활"),
    PARENTING("육아"),
    TRAVEL("여행"),
    ART("예술"),
    OTHER("기타");

    private final String value;

    SpecialtyProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpecialtyProperty fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (SpecialtyProperty category : SpecialtyProperty.values()) {
            if (category.getValue().equals(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}
