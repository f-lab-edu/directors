package com.directors.domain.room.exception;

import lombok.Getter;

@Getter
public class RoomNotFoundException extends RuntimeException {
    private Long roomId;
    private String requestUserId;
    private final static String message = "존재하지 않는 채팅방입니다.";

    public RoomNotFoundException(Long roomId, String requestUserId) {
        super(message);
        this.roomId = roomId;
        this.requestUserId = requestUserId;
    }
}
