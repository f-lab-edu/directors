package com.directors.application.room;

import com.directors.domain.question.Question;
import com.directors.domain.room.Room;
import com.directors.domain.room.exception.CannotCreateRoomException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.infrastructure.exception.question.QuestionNotFoundException;
import com.directors.presentation.room.CreateRoomRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CreateRoomTest extends RoomTestSupport {

    @DisplayName("질문 id와 요청시간, 디렉터 id를 통해 채팅방을 생성한다.")
    @Test
    void createRoom() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);
        User questioner = createUser("cnsong123", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule);
        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);

        Question question =
                createQuestion("원하는 질문", "질문 내용은 ~~~입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion = questionRepository.save(question);

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest request = createCreateRoomRequest(savedQuestion.getId(), requestTime);

        // when
        Long roomId = roomService.create(request, savedDirector.getId());

        // then
        assertThat(roomId).isNotNull();

        Room foundRoom = roomRepository.findById(roomId).orElseThrow(null);
        assertThat(foundRoom).extracting("question", "director", "questioner")
                .contains(savedQuestion, savedDirector, savedQuestioner);
    }


    @DisplayName("잘못된 질문 id로 채팅방 생성을 요청하면 예외가 발생한다.")
    @Test
    void createRoomWithWrongQuestionId() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);
        User questioner = createUser("cnsong123", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule);
        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);

        Question question =
                createQuestion("원하는 질문", "질문 내용은 ~~~입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        questionRepository.save(question);

        Long givenWrongQuestionId = -10L;

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest request = createCreateRoomRequest(givenWrongQuestionId, requestTime);

        // when
        assertThatThrownBy(() -> roomService.create(request, savedDirector.getId()))
                .isInstanceOf(QuestionNotFoundException.class);
    }

    @DisplayName("채팅 상태가 아닌 질문의 채팅방을 생성을 요청하면 예외가 발생한다.")
    @Test
    void createRoomAboutNotChatStatusQuestion() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);
        User questioner = createUser("cnsong123", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule);
        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);

        Question question =
                createQuestion("원하는 질문", "질문 내용은 ~~~입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion = questionRepository.save(question);

        savedQuestion.changeQuestionStatusToChat();

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest request = createCreateRoomRequest(savedQuestion.getId(), requestTime);

        // when
        assertThatThrownBy(() -> roomService.create(request, savedDirector.getId()))
                .isInstanceOf(CannotCreateRoomException.class)
                .hasMessage("채팅 방을 만들 수 없는 상태입니다.");
    }

    @DisplayName("질문의 스케줄 기간이 만료되었을 때 채팅방 생성을 요청하면 예외가 생성한다.")
    @Test
    void createRoomWithExpiredQuestionId() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);
        User questioner = createUser("cnsong123", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule);
        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);

        Question question =
                createQuestion("원하는 질문", "질문 내용은 ~~~입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion = questionRepository.save(question);

        LocalDateTime lateRequestTime = LocalDateTime.of(2023, 05, 9, 14, 1);

        CreateRoomRequest request = createCreateRoomRequest(savedQuestion.getId(), lateRequestTime);

        // when
        assertThatThrownBy(() -> roomService.create(request, savedDirector.getId()))
                .isInstanceOf(CannotCreateRoomException.class)
                .hasMessage("채팅 방을 만들 수 없는 상태입니다.");
    }
}