package com.directors.domain.user.exception;

public class NotEnoughRewardException extends RuntimeException {
	private final static String message = "유저의 지역 인증 내역이 존재하지 않습니다. 지역 인증을 진행해주세요.";
	public final String userId;

	public NotEnoughRewardException(String userId) {
		super(message);
		this.userId = userId;
	}
}
