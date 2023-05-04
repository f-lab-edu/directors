package com.directors.domain.feedback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.directors.domain.feedback.FeedbackRating.BEST;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class FeedbackRepositoryTest {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @DisplayName("피드백 아이디로 피드백을 조회한다.")
    @Test
    void findById() {
        // given
        var feedback = Feedback.builder()
                .feedbackRating(BEST)
                .description("최고의 피드백!")
                .build();

        Feedback savedFeedback = feedbackRepository.save(feedback);

        // when
        var findedFeedback = feedbackRepository.findById(savedFeedback.getId()).orElseThrow(null);
        // optional 해체의 책임은 어디있어야 할까?

        // then
        assertThat(findedFeedback)
                .extracting("feedbackRating", "description")
                .contains(BEST, "최고의 피드백!");
    }
}