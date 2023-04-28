package com.directors.domain.chat;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.room.Room;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Chat extends BaseEntity {
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

    public void setId(Long id) {
        this.id = id;
    }

    public static Chat of(Room room, String content, User sendUser) {
        return Chat.builder()
                .room(room)
                .content(content)
                .sendUser(sendUser)
                .build();
    }
}
