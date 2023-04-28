package com.directors.infrastructure.jpa.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryAdapter implements ChatRepository {

    private final JpaChatRepository jpaChatRepository;

    @Override
    public List<Chat> findChatListByRoomId(Long roomId, int offset, int size) {
        return jpaChatRepository.findListByRoomId(roomId, offset, size);
    }

    @Override
    public Chat save(Chat chat) {
        return jpaChatRepository.save(chat);
    }
}
