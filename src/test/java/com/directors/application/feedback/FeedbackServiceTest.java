package com.directors.application.feedback;

import com.directors.IntegrationTestSupport;
import com.directors.domain.feedback.exception.CannotCreateFeedbackException;
import com.directors.domain.feedback.exception.FeedbackNotFoundException;
import com.directors.domain.question.Question;
import com.directors.domain.question.QuestionRepository;
import com.directors.domain.question.QuestionStatus;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.feedback.request.CreateFeedbackRequest;
import com.directors.presentation.feedback.request.UpdateFeedbackRequest;
import com.directors.presentation.feedback.response.CreateFeedbackResponse;
import com.directors.presentation.feedback.response.GetByFeedbackIdResponse;
import com.directors.presentation.feedback.response.UpdateFeedbackResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedbackServiceTest extends IntegrationTestSupport {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    FeedbackService feedbackService;

    @DisplayName("피드백 데이터를 받아 피드백을 생성한다.")
    @Test
    void createFeedback() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");

        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));

        // when
        CreateFeedbackResponse response = feedbackService.create(request, questioner.getId());

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response)
                .extracting("directorId", "questionerId", "description", "rating")
                .contains("test1", "test2", "매우 좋았음.", "최고");
        assertThat(response.feedbackCheckedList())
                .isEqualTo(List.of("BS1", "BS2"));
    }

    @DisplayName("질문자의 아이디가 피드백의 질문자 아이디와 다르면 피드백이 생성되지 않는다.")
    @Test
    void createFeedbackWithDifferentQuestionerId() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");
        User outsider = createUser("test3", "123456789");
        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));

        // when // then
        assertThatThrownBy(() -> feedbackService.create(request, outsider.getId()))
                .isInstanceOf(CannotCreateFeedbackException.class)
                .hasMessage("피드백 생성에 대한 권한이 없습니다.");
    }

    @DisplayName("질문이 완료 상태가 아니면 피드백이 생성되지 않는다.")
    @Test
    void createFeedbackWithNotCompleteQuestion() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");
        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));

        // when // then
        assertThatThrownBy(() -> feedbackService.create(request, questioner.getId()))
                .isInstanceOf(CannotCreateFeedbackException.class)
                .hasMessage("피드백을 만들 수 없는 상태입니다.");
    }

    @DisplayName("존재하는 피드백의 내용을 수정한다.")
    @Test
    void updateFeedback() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");

        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));
        CreateFeedbackResponse response = feedbackService.create(request, questioner.getId());

        UpdateFeedbackRequest updateRequest = UpdateFeedbackRequest.builder()
                .feedbackId(response.id())
                .rating("보통")
                .description("매우 좋았음.")
                .checked(List.of("GD2", "GD4"))
                .build();

        // when
        UpdateFeedbackResponse updateResponse = feedbackService.update(updateRequest);

        // then
        assertThat(updateResponse.feedbackId()).isNotNull();
        assertThat(updateResponse)
                .extracting("rating", "description")
                .contains("보통", "매우 좋았음.");
        assertThat(updateResponse.checked())
                .isEqualTo(List.of("GD2", "GD4"));
    }

    @DisplayName("존재하지 않는 피드백 id를 사용해서 피드백을 수정하면 예외가 발생한다.")
    @Test
    void updateFeedbackWithWrongFeedbackId() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");

        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));
        CreateFeedbackResponse response = feedbackService.create(request, questioner.getId());

        UpdateFeedbackRequest updateRequest = UpdateFeedbackRequest.builder()
                .feedbackId(response.id() + 1)
                .rating("보통")
                .description("매우 좋았음.")
                .checked(List.of("GD2", "GD4"))
                .build();

        // when
        assertThatThrownBy(() -> feedbackService.update(updateRequest))
                .isInstanceOf(FeedbackNotFoundException.class)
                .hasMessage("존재하지 않는 피드백입니다.");
    }

    @DisplayName("피드백 id로 피드백을 조회한다.")
    @Test
    void getFeedbackByFeedbackId() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");

        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));
        CreateFeedbackResponse response = feedbackService.create(request, questioner.getId());

        // when
        GetByFeedbackIdResponse feedback = feedbackService.getFeedbackByFeedbackId(response.id());

        // then
        assertThat(feedback.feedbackId()).isNotNull();
        assertThat(feedback)
                .extracting("rating", "description")
                .contains("최고", "매우 좋았음.");
        assertThat(feedback.feedbackCheckedList())
                .isEqualTo(List.of("BS1", "BS2"));
    }

    @DisplayName("존재하지 않는 피드백 id로 피드백을 조회하면 예외가 발생한다.")
    @Test
    void getFeedbackByWrongFeedbackId() {
        // given
        User director = createUser("test1", "123456789");
        User questioner = createUser("test2", "123456789");

        Schedule schedule = createSchedule(director, LocalDateTime.of(2023, 5, 14, 14, 0));

        Question question = createQuestion("질문123", "궁금한 점.", director, questioner, schedule);
        question.changeQuestionStatusToComplete();

        CreateFeedbackRequest request = createCreateFeedbackRequest(question, "최고", "매우 좋았음.", List.of("BS1", "BS2"));
        CreateFeedbackResponse response = feedbackService.create(request, questioner.getId());

        // when // then
        assertThatThrownBy(() -> feedbackService.getFeedbackByFeedbackId(response.id() + 1))
                .isInstanceOf(FeedbackNotFoundException.class)
                .hasMessage("존재하지 않는 피드백입니다.");
    }

    private User createUser(String id, String password) {
        User user = User.builder()
                .id(id)
                .password(password)
                .userStatus(UserStatus.JOINED)
                .build();
        return userRepository.save(user);
    }

    private Schedule createSchedule(User director, LocalDateTime startTime) {
        Schedule schedule = Schedule.builder()
                .user(director)
                .status(ScheduleStatus.OPENED)
                .startTime(startTime)
                .build();
        return scheduleRepository.save(schedule);
    }

    private Question createQuestion(String title, String content, User director, User questioner, Schedule savedSchedule) {
        Question question = Question.builder()
                .title(title)
                .content(content)
                .status(QuestionStatus.WAITING)
                .questionCheck(false)
                .directorCheck(false)
                .questioner(questioner)
                .director(director)
                .category(SpecialtyProperty.BS)
                .schedule(savedSchedule)
                .build();

        return questionRepository.save(question);
    }

    private static CreateFeedbackRequest createCreateFeedbackRequest(Question question, String rating, String description, List<String> checkList) {
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .questionId(question.getId())
                .rating(rating)
                .description(description)
                .checkedList(checkList)
                .build();
        return request;
    }
}