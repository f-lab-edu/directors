package com.directors.infrastructure.jpa.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.chat.ChatRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.directors.domain.chat.QChat.chat;

@Repository
@RequiredArgsConstructor
public class ChatRepositoryAdapter implements ChatRepository {

    private final JpaChatRepository jpaChatRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Chat> findChatListByRoomId(Long roomId, int offset, int size) {
        return queryFactory.selectFrom(chat)
                .where(chat.room.id.eq(roomId))
                .orderBy(chat.createdTime.desc())
                .limit(size)
                .offset(offset)
                .fetch();
    }

    @Override
    public Chat save(Chat chat) {
        return jpaChatRepository.save(chat);
    }
}
