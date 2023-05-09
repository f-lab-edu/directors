package com.directors.application.room;

import com.directors.domain.question.Question;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.presentation.room.CreateRoomRequest;
import com.directors.presentation.room.response.GetRoomInfosByDirectorIdResponse;
import com.directors.presentation.room.response.GetRoomInfosByQuestionerIdResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class GetRoomTest extends RoomTestSupport {

    @DisplayName("디렉터 아이디를 통해 채팅방 정보를 조회한다.")
    @Test
    void getRoomInfosByDirectorId() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        User questioner1 = createUser("questioner1", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner1 = userRepository.save(questioner1);
        User questioner2 = createUser("questioner2", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner2 = userRepository.save(questioner2);

        LocalDateTime schedule1 = LocalDateTime.of(2023, 05, 9, 14, 0);
        LocalDateTime schedule2 = LocalDateTime.of(2023, 05, 10, 14, 0);
        List<LocalDateTime> scheduleList = List.of(schedule1, schedule2);
        scheduleService.open(savedDirector.getId(), scheduleList);

        Schedule savedSchedule1 = scheduleRepository.findByStartTimeAndUserId(schedule1, director.getId())
                .orElseThrow(null);
        Schedule savedSchedule2 = scheduleRepository.findByStartTimeAndUserId(schedule2, director.getId())
                .orElseThrow(null);

        Question question1 = createQuestion("원하는 질문", "질문 내용은 123 입니다.", savedDirector, savedQuestioner1, SpecialtyProperty.ART, savedSchedule1);
        Question savedQuestion1 = questionRepository.save(question1);
        Question question2 = createQuestion("원하는 질문", "질문 내용은 456 입니다.", savedDirector, savedQuestioner2, SpecialtyProperty.ART, savedSchedule2);
        Question savedQuestion2 = questionRepository.save(question2);

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest createRoomRequest1 = createCreateRoomRequest(savedQuestion1.getId(), requestTime);
        CreateRoomRequest createRoomRequest2 = createCreateRoomRequest(savedQuestion2.getId(), requestTime);

        Long roomId1 = roomService.create(createRoomRequest1, savedDirector.getId());
        Long roomId2 = roomService.create(createRoomRequest2, savedDirector.getId());

        // when
        List<GetRoomInfosByDirectorIdResponse> responses = roomService.getRoomInfosByDirectorId(savedDirector.getId());

        // then
        assertThat(responses).hasSize(2)
                .extracting("roomId", "questionId", "questionerId", "recentChatContent")
                .containsExactlyInAnyOrder(
                        tuple(roomId1, savedQuestion1.getId(), savedQuestioner1.getId(), "최근 채팅 내역이 존재하지 않습니다."),
                        tuple(roomId2, savedQuestion2.getId(), savedQuestioner2.getId(), "최근 채팅 내역이 존재하지 않습니다.")
                );
    }

    @DisplayName("회원가입 되지 않은 디렉터 아이디를 통해 채팅방 정보를 조회하면 예외가 발생한다.")
    @Test
    void getRoomInfosByNotRegisteredDirectorId() {
        // given
        String givenNotRegisteredDirectorId = "not_register_id";

        // when then
        assertThatThrownBy(() -> roomService.getRoomInfosByDirectorId(givenNotRegisteredDirectorId))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("질문자 아이디를 통해 채팅방 정보를 조회한다.")
    @Test
    void getRoomInfosByQuestionerId() {
        // given
        User questioner = createUser("questioner", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        User director1 = createUser("director1", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector1 = userRepository.save(director1);
        User director2 = createUser("director2", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector2 = userRepository.save(director2);

        LocalDateTime schedule1 = LocalDateTime.of(2023, 05, 9, 14, 0);
        LocalDateTime schedule2 = LocalDateTime.of(2023, 05, 10, 14, 0);
        List<LocalDateTime> scheduleList1 = List.of(schedule1);
        List<LocalDateTime> scheduleList2 = List.of(schedule2);
        scheduleService.open(savedDirector1.getId(), scheduleList1);
        scheduleService.open(savedDirector2.getId(), scheduleList2);

        Schedule savedSchedule1 = scheduleRepository.findByStartTimeAndUserId(schedule1, savedDirector1.getId())
                .orElseThrow(null);
        Schedule savedSchedule2 = scheduleRepository.findByStartTimeAndUserId(schedule2, savedDirector2.getId())
                .orElseThrow(null);

        Question question1 = createQuestion("원하는 질문1", "질문 내용은 123 입니다.", savedDirector1, savedQuestioner, SpecialtyProperty.ART, savedSchedule1);
        Question savedQuestion1 = questionRepository.save(question1);
        Question question2 = createQuestion("원하는 질문2", "질문 내용은 456 입니다.", savedDirector2, savedQuestioner, SpecialtyProperty.ART, savedSchedule2);
        Question savedQuestion2 = questionRepository.save(question2);

        LocalDateTime requestTime = LocalDateTime.of(2023, 05, 8, 14, 0);
        CreateRoomRequest createRoomRequest1 = createCreateRoomRequest(savedQuestion1.getId(), requestTime);
        CreateRoomRequest createRoomRequest2 = createCreateRoomRequest(savedQuestion2.getId(), requestTime);

        Long roomId1 = roomService.create(createRoomRequest1, savedDirector1.getId());
        Long roomId2 = roomService.create(createRoomRequest2, savedDirector2.getId());

        // when
        List<GetRoomInfosByQuestionerIdResponse> responses = roomService.getRoomInfosByQuestionerId(savedQuestioner.getId());

        // then
        assertThat(responses).hasSize(2)
                .extracting("roomId", "questionId", "directorId", "recentChatContent")
                .containsExactlyInAnyOrder(
                        tuple(roomId1, savedQuestion1.getId(), savedDirector1.getId(), "최근 채팅 내역이 존재하지 않습니다."),
                        tuple(roomId2, savedQuestion2.getId(), savedDirector2.getId(), "최근 채팅 내역이 존재하지 않습니다.")
                );
    }

    @DisplayName("회원가입 되지 않은 질문자 아이디를 통해 채팅방 정보를 조회하면 예외가 발생한다.")
    @Test
    void getRoomInfosByNotRegisteredQuestionerId() {
        // given
        String givenNotRegisteredQuestionerId = "not_register_id";

        // when then
        assertThatThrownBy(() -> roomService.getRoomInfosByQuestionerId(givenNotRegisteredQuestionerId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}