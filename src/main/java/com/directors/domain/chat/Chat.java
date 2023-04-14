package com.directors.domain.chat;

import com.directors.domain.user.User;
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
    private User sendUser;
    private User receiveUser;
    private LocalDateTime createTime;

    public void setId(Long id) {
        this.id = id;
    }

    public static com.directors.domain.chat.Chat of(Long roomId, String content, User sendUser, User receiveUser, LocalDateTime chatTime) {
        return Chat.builder()
                .roomId(roomId)
                .content(content)
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .createTime(chatTime)
                .build();
    }
}
