package com.directors.application.chat;

import com.directors.domain.question.Question;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
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

public class GetReceiveStreamTest extends ChatTestSupport {
    @DisplayName("채팅 스트림을 받는다.")
    @Test
    void getReceiveStream() {
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

        // when
        SseEmitter receiveStream = chatService.getReceiveStream(roomId, director.getId());

        // then
        assertThat(receiveStream)
                .isNotNull()
                .isInstanceOf(SseEmitter.class);
    }

    @DisplayName("생성되지 않은 roomId로 채팅 스트림을 요청하면 예외가 발생한다.")
    @Test
    void getReceiveStreamWithNotCreatedRoomId() {
        // given
        Long givenNotCreatedRoomId = 11L;
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        // when then
        assertThatThrownBy(() -> chatService.getReceiveStream(givenNotCreatedRoomId, savedDirector.getId()))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }
}
