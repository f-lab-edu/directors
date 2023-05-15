package com.directors.application.chat;

import com.directors.domain.chat.Chat;
import com.directors.domain.question.Question;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.presentation.chat.request.SendChatRequest;
import com.directors.presentation.room.CreateRoomRequest;
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

public class SendTest extends ChatTestSupport {

    @DisplayName("채팅방에 채팅을 전송한다.")
    @Test
    void sendChat() {
        // given
        String givenChatContent = "채팅을 보냅니다.";

        doAnswer((Answer<Void>) invocation -> null)
                .when(liveChatManager).addReceiver(any(Long.class), any(SseEmitter.class));
        doAnswer((Answer<Void>) invocation -> null)
                .when(liveChatManager).sendChat(any(Long.class), any(String.class), any(LocalDateTime.class), any(String.class));

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

        SendChatRequest request = SendChatRequest.builder()
                .roomId(roomId)
                .chatContent(givenChatContent)
                .build();
        // when
        boolean isSend = chatService.sendChat(request, savedDirector.getId());

        // then
        assertThat(isSend).isTrue();
        List<Chat> chatList = chatRepository.findChatListByRoomId(roomId, 0, 1);
        assertThat(chatList.get(0)).extracting("content").isEqualTo(givenChatContent);
    }

    @DisplayName("생성되지 않은 채팅방 아이디로 채팅 전송을 요청하면 예외가 발생한다.")
    @Test
    void sendChatWithNotCreateRoomId() {
        // given
        Long givenNotCreatedRoomId = 10L;

        doAnswer((Answer<Void>) invocation -> null)
                .when(liveChatManager).addReceiver(any(Long.class), any(SseEmitter.class));
        doAnswer((Answer<Void>) invocation -> null)
                .when(liveChatManager).sendChat(any(Long.class), any(String.class), any(LocalDateTime.class), any(String.class));

        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        SendChatRequest request = SendChatRequest.builder()
                .roomId(givenNotCreatedRoomId)
                .chatContent("givenChatContent")
                .build();

        // when then
        assertThatThrownBy(() -> chatService.sendChat(request, savedDirector.getId()))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }
}
