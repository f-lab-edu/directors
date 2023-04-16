package com.directors.domain.chat;

import java.util.List;

public interface ChatRepository {
    List<Chat> findChatListByRoomId(Long roomId, Long offset, Long size);

    void save(Chat chat);
}
