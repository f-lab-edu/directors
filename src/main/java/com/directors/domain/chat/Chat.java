package com.directors.domain.chat;

import com.directors.domain.common.BaseEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "send_user_id")
    private User sendUser;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdTime;

    public void setId(Long id) {
        this.id = id;
    }

    public static Chat of(Room room, String content, User sendUser, LocalDateTime sendTime) {
        return Chat.builder()
                .room(room)
                .content(content)
                .sendUser(sendUser)
                .createdTime(sendTime)
                .build();
    }
}
