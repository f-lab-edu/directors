package com.directors.domain.chat;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface LiveChatManager {
    void addReceiver(Long roomId, SseEmitter sseEmitter);

    void removeReceiver(Long roomId, SseEmitter sseEmitter);

    void sendMessage(Long roomId, String chatContent, String sendUserId);
}
