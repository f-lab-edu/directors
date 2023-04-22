package com.directors.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class Chat {
    private Long id;
    private Long roomId;
    private String content;
    private String sendUserId;
    private LocalDateTime createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public static Chat of(Long roomId, String content, String sendUserId, LocalDateTime chatTime) {
        return Chat.builder()
                .roomId(roomId)
                .content(content)
                .sendUserId(sendUserId)
                .createTime(chatTime)
                .build();
    }
}
