package com.directors.infrastructure.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.LiveChatManager;
import com.directors.infrastructure.util.ObjectMapperUtils;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LiveChatManagerImpl implements LiveChatManager, MessageListener {
    private Map<Long, List<SseEmitter>> sseListMap = new HashMap<>();
    private Map<Long, ChannelTopic> channels = new HashMap<>();

    private final RedisMessageListenerContainer listenerContainer;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = getBodyString(message.getBody());
        Chat chat = ObjectMapperUtils.readValue(messageBody, Chat.class);

        publishChatToEmitters(chat.getRoomId(), chat);
    }

    @Override
    public void addReceiver(Long roomId, SseEmitter sseEmitter) {
        if (!channels.containsKey(roomId)) {
            addTopicToListener(roomId);
        }

        if (!sseListMap.containsKey(roomId)) {
            makeListAndAddEmitter(roomId, sseEmitter);
        } else {
            addEmitterToList(roomId, sseEmitter);
        }
    }

    @Override
    public void removeReceiver(Long roomId, SseEmitter sseEmitter) {
        if (!sseListMap.containsKey(roomId)) {
            return;
        }

        var emitters = sseListMap.get(roomId);
        emitters.remove(sseEmitter);

        if (emitters.isEmpty()) {
            sseListMap.remove(emitters);
            removeTopicToListener(roomId);
        }

        log.info("roomId " + roomId + " | User left. currentUser =>" + emitters.size());
    }

    @Override
    public void sendChat(Long roomId, String chatContent, String sendUserId, LocalDateTime sendTime)  {
        if (!sseListMap.containsKey(roomId)) {
            return;
        }

        Chat chat = Chat.of(roomId, chatContent, sendUserId, sendTime);

        redisTemplate.convertAndSend(String.valueOf(roomId), ObjectMapperUtils.writeValueAsString(chat));
    }

    private String getBodyString(byte[] body) {
        String newBody = new String(body).replace("\\", "");
        return newBody.substring(1, newBody.length() - 1);
    }

    private void publishChatToEmitters(Long roomId, Chat chat) {
        List<SseEmitter> emitterList = sseListMap.get(roomId);

        if (emitterList.isEmpty()) {
            return;
        }

        try {
            for (SseEmitter sseEmitter : emitterList) {
                sseEmitter.send(chat);
            }
        } catch (IOException e) {
            log.warn("Failed to send chat via SSE. roomId: " + roomId);
        }
    }

    private void addTopicToListener(Long roomId) {
        var channel = new ChannelTopic(String.valueOf(roomId));
        listenerContainer.addMessageListener(this, channel);
        channels.put(roomId, channel);
    }

    private void removeTopicToListener(Long roomId) {
        var channel = channels.get(roomId);
        listenerContainer.removeMessageListener(this, channel);
        channels.remove(roomId);
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
