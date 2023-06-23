package com.directors.presentation.chat.response;

import com.directors.domain.chat.Chat;

import java.time.LocalDateTime;

public record ChatResponse(
        Long roomId,
        String content,
        String sendUserId,
        LocalDateTime createTime
) {
    public static ChatResponse from(Long roomId, Chat chat) {
        return new ChatResponse(roomId, chat.getContent(), chat.getSendUserId(), chat.getSendTime());
    }
}
