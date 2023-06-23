package com.directors.domain.chat;

import com.directors.domain.room.Room;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long roomId;

    private String content;

    private String  sendUserId;

    @Column(updatable = false, nullable = false)
    private LocalDateTime sendTime;

    public void setId(Long id) {
        this.id = id;
    }

    public static Chat of(Long roomId, String content, String sendUserId, LocalDateTime sendTime) {
        return Chat.builder()
                .roomId(roomId)
                .content(content)
                .sendUserId(sendUserId)
                .sendTime(sendTime)
                .build();
    }
}
