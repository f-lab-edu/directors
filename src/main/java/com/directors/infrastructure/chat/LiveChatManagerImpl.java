package com.directors.infrastructure.chat;

import com.directors.domain.chat.LiveChatManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveChatManagerImpl implements LiveChatManager {
    // TODO: 04.14 @Transaction 적용 여부 판단 필요
    Map<Long, List<SseEmitter>> sseListMap = new HashMap<>();

    public void addReceiver(Long roomId, SseEmitter sseEmitter) {
        if (!sseListMap.containsKey(roomId)) {
            makeListAndAddEmitter(roomId, sseEmitter);
        } else {
            addEmitterToList(roomId, sseEmitter);
        }
    }

    public void sendMessage(Long roomId, String chatContent, String sendUserId) {
        if (!sseListMap.containsKey(roomId)) {
            return;
        }

        List<SseEmitter> emitterList = sseListMap.get(roomId);

        if (emitterList.isEmpty()) {
            return;
        }

        try {
            for (SseEmitter sseEmitter : emitterList) {
                sseEmitter.send(chatContent); // data type text 필요. default => MediaType.APPLICATION_JSON
            }
        } catch (IOException e) {
            log.warn("Failed to send chat via SSE. roomId: " + roomId);
        }
    }

    public void removeReceiver(Long roomId, SseEmitter sseEmitter) {
        List<SseEmitter> emitters = sseListMap.get(roomId);
        emitters.remove(sseEmitter);

        log.info("roomId " + roomId + " | 1 User left. currentUser =>" + emitters.size());
    }

    private void makeListAndAddEmitter(Long roomId, SseEmitter sseEmitter) {
        List<SseEmitter> emitterList = new ArrayList<>();
        emitterList.add(sseEmitter);
        sseListMap.put(roomId, emitterList);
    }

    private void addEmitterToList(Long roomId, SseEmitter sseEmitter) {
        List<SseEmitter> emitterList = sseListMap.get(roomId);
        emitterList.add(sseEmitter);
    }
}
