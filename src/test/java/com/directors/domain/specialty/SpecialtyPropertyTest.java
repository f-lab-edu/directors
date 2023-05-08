package com.directors.domain.specialty;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecialtyPropertyTest {

    @DisplayName("전문 분야 스트링을 전문 분야 객체로 변환한다.")
    @Test
    void fromValue() {
        // given
        String givenSpecialtyPropertyString = "프로그래밍";

        // when
        SpecialtyProperty specialtyProperty = SpecialtyProperty.fromValue(givenSpecialtyPropertyString);

        // then
        assertThat(specialtyProperty.getValue()).isNotNull()
                .isEqualTo(givenSpecialtyPropertyString);
    }

    @DisplayName("잘못된 전문 분야 스트링을 전문 분야 객체로 변환 시도할 경우 예외가 발생한다..")
    @Test
    void fromWrongValue() {
        // given
        String givenSpecialtyPropertyString = "그래";

        // when then
        assertThatThrownBy(() -> SpecialtyProperty.fromValue(givenSpecialtyPropertyString))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid value: " + givenSpecialtyPropertyString);
    }

}