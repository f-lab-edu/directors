package com.directors.infrastructure.jpa.chat;

import com.directors.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository extends JpaRepository<Chat, Long> {
}
