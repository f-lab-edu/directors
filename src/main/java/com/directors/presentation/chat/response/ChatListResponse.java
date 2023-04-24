package com.directors.presentation.chat.response;

import com.directors.domain.chat.Chat;

import java.time.LocalDateTime;

public record ChatListResponse(
        Long roomId,
        String content,
        String sendUserId,
        LocalDateTime createTime
) {
    public static ChatListResponse from(Chat chat) {
        return new ChatListResponse(chat.getRoomId(), chat.getContent(), chat.getSendUserId(), chat.getCreateTime());
    }
}
