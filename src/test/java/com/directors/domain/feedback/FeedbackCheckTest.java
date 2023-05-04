package com.directors.domain.feedback;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FeedbackCheckTest {

    @DisplayName("value가 피드백 체크 타입에 속했는지 체크한다.")
    @Test
    void fromValueSuccess() {
        // given
        FeedbackCheck givenType = FeedbackCheck.GOOD_MANNER;
        String givenTypeValue = givenType.getValue();

        // when
        FeedbackCheck check = FeedbackCheck.fromValue(givenTypeValue);

        // then
        assertThat(check).isEqualTo(givenType);
    }

    @DisplayName("value가 피드백 체크 타입에 속하지 않으면 예외가 발생하는지 체크한다.")
    @Test
    void fromValueFail() {
        // given
        FeedbackCheck givenType = FeedbackCheck.GOOD_MANNER;
        String givenTypeValue = givenType.getValue() + "WRONG_VALUE";

        // when // then
        assertThatThrownBy(() -> FeedbackCheck.fromValue(givenTypeValue))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid value: " + givenTypeValue);
    }
}