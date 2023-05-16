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
    Map<Long, List<SseEmitter>> sseListMap = new HashMap<>();
    private final RedisMessageListenerContainer listenerContainer;

    private final RedisTemplate<String, Object> redisTemplate;
    private Map<String, ChannelTopic> channels;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String body = getBodyString(message.getBody());
        Chat chat = ObjectMapperUtils.readValue(body, Chat.class);

        publishChatToRoom(chat.getRoomId(), chat);
    }

    // redis listener add / list 0 -> remove
    public void addReceiver(Long roomId, SseEmitter sseEmitter) {
        ChannelTopic channel = new ChannelTopic(String.valueOf(roomId));
        listenerContainer.addMessageListener(this, channel);

        if (!sseListMap.containsKey(roomId)) {
            makeListAndAddEmitter(roomId, sseEmitter);
        } else {
            addEmitterToList(roomId, sseEmitter);
        }
    }

    public void sendChat(Long roomId, String chatContent, String sendUserId, LocalDateTime sendTime)  {
        if (!sseListMap.containsKey(roomId)) {
            return;
        }

        Chat chat = Chat.of(roomId, chatContent, sendUserId, sendTime);

        redisTemplate.convertAndSend(String.valueOf(roomId), ObjectMapperUtils.writeValueAsString(chat));
    }

    public void removeReceiver(Long roomId, SseEmitter sseEmitter) {
        if (!sseListMap.containsKey(roomId)) {
            return;
        }

        List<SseEmitter> emitters = sseListMap.get(roomId);
        emitters.remove(sseEmitter);



        log.info("roomId " + roomId + " | 1 User left. currentUser =>" + emitters.size());
    }

    private String getBodyString(byte[] body) {
        String newBody = new String(body).replace("\\", "");
        return newBody.substring(1, newBody.length() - 1);
    }

    private void publishChatToRoom(Long roomId, Chat chat) {
        List<SseEmitter> emitterList = sseListMap.get(roomId);

        if (emitterList.isEmpty()) {
            return;
        }

        try {
            for (SseEmitter sseEmitter : emitterList) {
                sseEmitter.send(chat); // data type text 필요. default => MediaType.APPLICATION_JSON
            }
        } catch (IOException e) {
            log.warn("Failed to send chat via SSE. roomId: " + roomId);
        }
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
