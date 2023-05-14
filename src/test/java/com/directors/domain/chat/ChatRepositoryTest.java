package com.directors.domain.chat;

import com.directors.IntegrationTestSupport;
import com.directors.application.chat.ChatService;
import com.directors.application.room.RoomService;
import com.directors.application.schedule.ScheduleService;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.room.Room;
import com.directors.domain.room.RoomRepository;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.room.CreateRoomRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

class ChatRepositoryTest extends IntegrationTestSupport {
    @Autowired
    ChatService chatService;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    RoomService roomService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    RoomRepository roomRepository;

    @MockBean
    LiveChatManager liveChatManager;

    @DisplayName("채팅방 아이디로 채팅 목록을 조회한다.")
    @Test
    void findChatListByRoomId() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        User questioner = createUser("questioner", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule);

        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);

        Question question = createQuestion("원하는 질문", "질문 내용은 123 입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion = questionRepository.save(question);

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest createRoomRequest = createCreateRoomRequest(savedQuestion.getId(), requestTime);
        Long roomId = roomService.create(createRoomRequest, savedDirector.getId());

        doAnswer((Answer<Void>) invocation -> null)
                .when(liveChatManager).addReceiver(any(Long.class), any(SseEmitter.class));

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException(roomId, savedDirector.getId()));

        Chat chat1 = Chat.of(room, "chatContent123", savedDirector);
        Chat chat2 = Chat.of(room, "chatContent456", savedDirector);
        Chat chat3 = Chat.of(room, "chatContent789", savedDirector);

        chatRepository.saveAll(List.of(chat1, chat2, chat3));

        // when
        List<Chat> chatList = chatRepository.findChatListByRoomId(roomId, 0, 10);

        // then
        assertThat(chatList).hasSize(3)
                .extracting("content")
                .containsExactlyInAnyOrder(
                        "chatContent123", "chatContent456", "chatContent789"
                );
    }

    @DisplayName("생성되지 않은 채팅방 아이디로 채팅 목록을 조회하면 빈 배열을 반환한다.")
    @Test
    void findChatListByNotCreatedRoomId() {
        // given
        Long givenWrongRoomId = -1L;

        // when then
        assertThat(chatRepository.findChatListByRoomId(givenWrongRoomId, 0, 10))
                .isNotNull().hasSize(0);
    }

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

    public static CreateRoomRequest createCreateRoomRequest(Long questionId, LocalDateTime requestTime) {
        return CreateRoomRequest.builder()
                .questionId(questionId)
                .requestTime(requestTime)
                .build();
    }
}