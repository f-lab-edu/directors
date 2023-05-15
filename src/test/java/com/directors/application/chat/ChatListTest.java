package com.directors.application.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.question.Question;
import com.directors.domain.room.Room;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.presentation.chat.request.ChatListRequest;
import com.directors.presentation.chat.response.ChatListResponse;
import com.directors.presentation.room.CreateRoomRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class ChatListTest extends ChatTestSupport {

    @DisplayName("채팅 방의 채팅 목록을 조회한다.")
    @Test
    void chatList() {
        // given
        String givenContent1 = "chatContent123";
        String givenContent2 = "chatContent456";
        String givenContent3 = "chatContent789";

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

        Chat chat1 = Chat.of(room.getId(), givenContent1, savedDirector, null);
        Chat chat2 = Chat.of(room.getId(), givenContent2, savedDirector, null);
        Chat chat3 = Chat.of(room.getId(), givenContent3, savedDirector, null);

        chatRepository.saveAll(List.of(chat1, chat2, chat3));

        ChatListRequest request = ChatListRequest.builder()
                .roomId(roomId)
                .userId(savedDirector.getId())
                .offset(0)
                .size(10).build();

        // when
        List<ChatListResponse> responses = chatService.chatList(request);

        // then
        assertThat(responses).hasSize(3)
                .extracting("roomId", "content", "sendUserId")
                .containsExactlyInAnyOrder(
                        Assertions.tuple(roomId, givenContent1, savedDirector.getId()),
                        Assertions.tuple(roomId, givenContent2, savedDirector.getId()),
                        Assertions.tuple(roomId, givenContent3, savedDirector.getId())
                );
    }

    @DisplayName("생성되지 않은 채팅방 아이디로 채팅 목록을 조회하면 예외가 발생한다.")
    @Test
    void chatListWithWrongRoomId() {
        // given
        Long givenWrongRoomId = 10L;
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        ChatListRequest request = ChatListRequest.builder()
                .roomId(givenWrongRoomId)
                .userId(savedDirector.getId())
                .offset(0)
                .size(10).build();

        // when then
        assertThatThrownBy(() -> chatService.chatList(request))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }
}
