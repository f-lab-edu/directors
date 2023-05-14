package com.directors.application.chat;

import com.directors.IntegrationTestSupport;
import com.directors.application.room.RoomService;
import com.directors.application.schedule.ScheduleService;
import com.directors.domain.chat.ChatRepository;
import com.directors.domain.chat.LiveChatManager;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.room.CreateRoomRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

public class ChatTestSupport extends IntegrationTestSupport {
    @Autowired
    ChatService chatService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    RoomService roomService;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    RoomRepository roomRepository;

    @MockBean
    LiveChatManager liveChatManager;

    protected User createUser(String id, String password, String email, String phoneNumber, String name, String nickname) {
        return User.builder()
                .id(id)
                .password(password)
                .userStatus(UserStatus.JOINED)
                .email(email)
                .phoneNumber(phoneNumber)
                .name(name)
                .reward(0L)
                .nickname(nickname)
                .build();
    }

    protected Question createQuestion(String title, String content, User director, User questioner, SpecialtyProperty property, Schedule schedule) {
        return Question.builder()
                .title(title)
                .content(content)
                .status(QuestionStatus.WAITING)
                .questionCheck(false)
                .directorCheck(false)
                .director(director)
                .questioner(questioner)
                .category(property)
                .schedule(schedule)
                .build();
    }

    protected static CreateRoomRequest createCreateRoomRequest(Long questionId, LocalDateTime requestTime) {
        return CreateRoomRequest.builder()
                .questionId(questionId)
                .requestTime(requestTime)
                .build();
    }
}
