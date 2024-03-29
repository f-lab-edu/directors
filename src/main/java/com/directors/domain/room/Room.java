package com.directors.domain.room;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.question.Question;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Room extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "director_id")
    private User director;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "questioner_id")
    private User questioner;

    @Builder
    public Room(Question question, User director, User questioner) {
        this.question = question;
        this.director = director;
        this.questioner = questioner;
    }

    public static Room of(Question question, User director, User questioner) {
        return Room.builder()
                .question(question)
                .director(director)
                .questioner(questioner)
                .build();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void validateRoomUser(String sendUserId) {
        if (!(sendUserId.equals(this.director.getId()) || sendUserId.equals(this.questioner.getId()))) {
            throw new RoomNotFoundException(this.id, sendUserId);
        }
    }
}
