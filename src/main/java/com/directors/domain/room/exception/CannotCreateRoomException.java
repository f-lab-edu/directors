package com.directors.domain.room.exception;

public class CannotCreateRoomException extends RuntimeException {
    public final static String STATUS = "채팅 방을 만들 수 없는 상태입니다.";
    public final static String AUTH = "채팅 방 생성에 대한 권한이 없습니다.";

    public final Long questionId;

    public CannotCreateRoomException(Long questionId, String message) {
        super(message);
        this.questionId = questionId;
    }
}
