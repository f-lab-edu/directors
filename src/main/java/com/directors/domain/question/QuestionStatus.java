package com.directors.domain.question;

public enum QuestionStatus {
	WAITING("기다리는 중"),
	CHATTING("채팅 중"),
	COMPLETE("약속 완료");

	private final String value;

	QuestionStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
