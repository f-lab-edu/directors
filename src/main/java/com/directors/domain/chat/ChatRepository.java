package com.directors.domain.chat;

import java.util.List;

public interface ChatRepository {
    List<Chat> findChatListByRoomId(Long roomId, int offset, int size);

    void save(Chat chat);
}
