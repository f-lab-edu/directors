package com.directors.domain.chat;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;

public interface LiveChatManager {
    void addReceiver(Long roomId, SseEmitter sseEmitter);

    void removeReceiver(Long roomId, SseEmitter sseEmitter);

    void sendChat(Long roomId, String chatContent, LocalDateTime sendTime, String sendUserId);
}
