package com.directors.infrastructure.jpa.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InmemoryChatRepository implements ChatRepository {
    Map<Long, Chat> chatmap = new HashMap<>();

    @Override
    public List<Chat> findChatListByRoomId(Long roomId, int offset, int limit) {
        List<Chat> chatList = new ArrayList<>();

//        List<Chat> chatByRoomId = chatmap.values()
//                .stream()
//                .filter(v -> v.getRoomId().equals(roomId))
//                .collect(Collectors.toList());
//
//        for (int i = offset; i < offset + size; i++) {
//            chatList.add(chatByRoomId.get(i));
//        }

        return chatList;
    }

    @Override
    public Chat save(Chat chat) {
        chatmap.put(chat.getId(), chat);
        return chat;
    }
}
