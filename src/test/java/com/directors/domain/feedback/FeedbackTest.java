package com.directors.domain.feedback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.directors.domain.feedback.FeedbackCheck.PROFESSIONAL;
import static com.directors.domain.feedback.FeedbackCheck.TIMELINESS_MET;
import static com.directors.domain.feedback.FeedbackRating.BEST;
import static org.assertj.core.api.Assertions.assertThat;

class FeedbackTest {

    @DisplayName("피드백이 가지고 있는 체크리스트의 값들을 조회한다")
    @Test
    void getFeedbackCheckValues() {
        // given
        List<FeedbackCheck> givenFeedbackCheckList = List.of(TIMELINESS_MET, PROFESSIONAL);

        Feedback feedback = Feedback.builder()
                .description("매우 잘함.")
                .feedbackRating(BEST)
                .feedbackCheckList(givenFeedbackCheckList)
                .build();

        // when
        List<FeedbackCheck> feedbackCheckList = feedback.getFeedbackCheckList();

        // then
        assertThat(feedbackCheckList).hasSize(2)
                .isEqualTo(givenFeedbackCheckList);
    }
}