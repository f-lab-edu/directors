package com.directors.domain.room;

import com.directors.IntegrationTestSupport;
import com.directors.application.schedule.ScheduleService;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.room.exception.RoomNotFoundException;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class RoomRepositoryTest extends IntegrationTestSupport {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    QuestionRepository questionRepository;

    @DisplayName("생성된 채팅방을 채팅방 id를 통해 조회한다.")
    @Test
    void findById() {
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

        var room = Room.of(question, question.getDirector(), question.getQuestioner());
        Room savedRoom = roomRepository.save(room);

        // when
        Room foundRoom = roomRepository.findById(savedRoom.getId())
                .orElseThrow(null);

        // then
        assertThat(foundRoom.getId()).isNotNull();
        assertThat(foundRoom).extracting("question", "director", "questioner")
                .contains(savedQuestion, savedDirector, savedQuestioner);
    }

    @DisplayName("생성되지 않은 채팅방을 조회하면 예외가 발생한다.")
    @Test
    void findByIdWithNotCreatedRoom() {
        // given
        Long givenNotCreatedRoomId = 10L;

        // when then
        assertThatThrownBy(() -> roomRepository.findById(givenNotCreatedRoomId)
                .orElseThrow(() -> new RoomNotFoundException(givenNotCreatedRoomId, "userId")))
                .isInstanceOf(RoomNotFoundException.class)
                .hasMessage("존재하지 않는 채팅방입니다.");
    }

    @DisplayName("생성된 채팅방을 디렉터 id를 통해 조회한다.")
    @Test
    void findByDirectorId() {
        // given
        User director = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector = userRepository.save(director);

        User questioner = createUser("cnsong123", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        User questioner2 = createUser("cnsong456", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner2 = userRepository.save(questioner2);

        LocalDateTime schedule = LocalDateTime.of(2023, 05, 9, 14, 0);
        LocalDateTime schedule2 = LocalDateTime.of(2023, 05, 9, 15, 0);

        List<LocalDateTime> scheduleList = List.of(schedule, schedule2);
        scheduleService.open(savedDirector.getId(), scheduleList);
        Schedule savedSchedule = scheduleRepository.findByStartTimeAndUserId(schedule, director.getId())
                .orElseThrow(null);
        Schedule savedSchedule2 = scheduleRepository.findByStartTimeAndUserId(schedule2, director.getId())
                .orElseThrow(null);

        Question question =
                createQuestion("원하는 질문", "질문 내용은 123입니다.", savedDirector, savedQuestioner, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion = questionRepository.save(question);
        Question question2 =
                createQuestion("원하는 질문", "질문 내용은 456입니다.", savedDirector, savedQuestioner2, SpecialtyProperty.ART, savedSchedule);
        Question savedQuestion2 = questionRepository.save(question2);

        var room = Room.of(savedQuestion, savedQuestion.getDirector(), savedQuestion.getQuestioner());
        roomRepository.save(room);
        var room2 = Room.of(savedQuestion2, savedQuestion2.getDirector(), savedQuestion2.getQuestioner());
        roomRepository.save(room2);

        // when
        List<Room> foundRooms = roomRepository.findByDirectorId(savedDirector.getId());

        // then
        assertThat(foundRooms).hasSize(2)
                .extracting("question", "director", "questioner")
                .containsExactlyInAnyOrder(
                        tuple(savedQuestion, savedDirector, savedQuestioner),
                        tuple(savedQuestion2, savedDirector, savedQuestioner2)
                );
    }

    @DisplayName("존재하지 않는 디렉터 id나 room을 생성하지 않은 디렉터 id를 통해 생성된 채팅방을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findByWrongDirectorId() {
        // given
        String givenSavedDirectorId = "wrong_id";

        // when
        List<Room> emptyRooms = roomRepository.findByDirectorId(givenSavedDirectorId);

        // then
        assertThat(emptyRooms).isEmpty();
    }

    @DisplayName("생성된 채팅방을 질문자 id를 통해 조회한다.")
    @Test
    void findByQuestionerId() {
        User director = createUser("director1", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector1 = userRepository.save(director);
        User director2 = createUser("director2", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedDirector2 = userRepository.save(director2);

        User questioner = createUser("questioner", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedQuestioner = userRepository.save(questioner);

        LocalDateTime schedule1 = LocalDateTime.of(2023, 05, 9, 14, 0);
        LocalDateTime schedule2 = LocalDateTime.of(2023, 05, 9, 15, 0);

        List<LocalDateTime> scheduleList1 = List.of(schedule1);
        scheduleService.open(savedDirector1.getId(), scheduleList1);
        Schedule savedSchedule1 = scheduleRepository.findByStartTimeAndUserId(schedule1, savedDirector1.getId())
                .orElseThrow(null);

        List<LocalDateTime> scheduleList2 = List.of(schedule2);
        scheduleService.open(savedDirector2.getId(), scheduleList2);
        Schedule savedSchedule2 = scheduleRepository.findByStartTimeAndUserId(schedule2, savedDirector2.getId())
                .orElseThrow(null);

        Question question1 =
                createQuestion("원하는 질문", "질문 내용은 123입니다.", savedDirector1, savedQuestioner, SpecialtyProperty.ART, savedSchedule1);
        Question savedQuestion1 = questionRepository.save(question1);
        Question question2 =
                createQuestion("원하는 질문", "질문 내용은 456입니다.", savedDirector2, savedQuestioner, SpecialtyProperty.ART, savedSchedule2);
        Question savedQuestion2 = questionRepository.save(question2);

        var room = Room.of(savedQuestion1, savedQuestion1.getDirector(), savedQuestion1.getQuestioner());
        roomRepository.save(room);
        var room2 = Room.of(savedQuestion2, savedQuestion2.getDirector(), savedQuestion2.getQuestioner());
        roomRepository.save(room2);

        // when
        List<Room> foundRooms = roomRepository.findByQuestionerId(savedQuestioner.getId());

        // then
        assertThat(foundRooms).hasSize(2)
                .extracting("question", "director", "questioner")
                .containsExactlyInAnyOrder(
                        tuple(savedQuestion1, savedDirector1, savedQuestioner),
                        tuple(savedQuestion2, savedDirector2, savedQuestioner)
                );
    }

    @DisplayName("존재하지 않는 질문자 id를 통해 생성된 채팅방을 조회하면 빈 리스트를 반환한다.")
    @Test
    void findByWrongQuestionerId() {
        // given
        String givenSavedQuestionerId = "wrong_id";

        // when
        List<Room> emptyRooms = roomRepository.findByQuestionerId(givenSavedQuestionerId);

        // then
        assertThat(emptyRooms).isEmpty();
    }

    private User createUser(String id, String password, String email, String phoneNumber, String name, String nickname) {
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

    private Question createQuestion(String title, String content, User director, User questioner, SpecialtyProperty property, Schedule schedule) {
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
}