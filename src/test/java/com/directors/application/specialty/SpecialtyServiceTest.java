package com.directors.application.specialty;

import com.directors.IntegrationTestSupport;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.domain.specialty.exception.NoSuchSpecialtyException;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.specialty.request.CreateSpecialtyRequest;
import com.directors.presentation.specialty.request.UpdateSpecialtyRequest;
import com.directors.presentation.specialty.response.UpdateSpecialtyResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SpecialtyServiceTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpecialtyService specialtyService;

    @Autowired
    SpecialtyRepository specialtyRepository;

    @DisplayName("유저의 전문 분야를 생성한다.")
    @Test
    void createSpecialty() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);
        String givenSpecialtyProperty = "프로그래밍";
        String givenDescription = "백엔드 전문가";

        var request = CreateSpecialtyRequest.builder()
                .specialtyProperty(givenSpecialtyProperty)
                .description(givenDescription)
                .build();

        // when
        var response = specialtyService.createSpecialty(request, savedUser.getId());

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response).extracting("specialtyProperty", "description")
                .contains(givenSpecialtyProperty, givenDescription);
    }

    @DisplayName("존재하지 않는 전문분야 생성 요청 시 예외가 발생한다.")
    @Test
    void createSpecialtyWithWrongSpecialty() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);
        String givenSpecialtyProperty = "빨리 걷기";
        String givenDescription = "백엔드 전문가";

        var request = CreateSpecialtyRequest.builder()
                .specialtyProperty(givenSpecialtyProperty)
                .description(givenDescription)
                .build();

        // when then
        assertThatThrownBy(() -> specialtyService.createSpecialty(request, savedUser.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid value: " + givenSpecialtyProperty);
    }

    @DisplayName("전문 분야를 업데이트한다.")
    @Test
    void updateSpecialty() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        String oldSpecialtyProperty = "프로그래밍";
        String oldDescription = "백엔드 전문가";
        String newSpecialtyProperty = "교육";
        String newDescription = "영어 교육 10년차";

        var createSpecialtyRequest = CreateSpecialtyRequest.builder()
                .specialtyProperty(oldSpecialtyProperty)
                .description(oldDescription)
                .build();
        var savedSpecialty = specialtyService
                .createSpecialty(createSpecialtyRequest, savedUser.getId());

        UpdateSpecialtyRequest request = UpdateSpecialtyRequest.builder()
                .id(savedSpecialty.id())
                .property(newSpecialtyProperty)
                .description(newDescription)
                .build();

        // when
        UpdateSpecialtyResponse response = specialtyService.updateSpecialty(request);

        // then
        assertThat(response).extracting("id", "property", "description")
                .contains(savedSpecialty.id(), newSpecialtyProperty, newDescription);
    }

    @DisplayName("존재하지 않는 전문분야 아이디로 업데이트 요청 시 예외가 발생한다.")
    @Test
    void updateSpecialtyWithWrongSpecialtyId() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        String oldSpecialtyProperty = "프로그래밍";
        String oldDescription = "백엔드 전문가";
        String newSpecialtyProperty = "교육";
        String newDescription = "영어 교육 10년차";

        var createSpecialtyRequest = CreateSpecialtyRequest.builder()
                .specialtyProperty(oldSpecialtyProperty)
                .description(oldDescription)
                .build();
        var savedSpecialty = specialtyService
                .createSpecialty(createSpecialtyRequest, savedUser.getId());

        UpdateSpecialtyRequest request = UpdateSpecialtyRequest.builder()
                .id(savedSpecialty.id() + 1)
                .property(newSpecialtyProperty)
                .description(newDescription)
                .build();

        // when
        assertThatThrownBy(() -> specialtyService.updateSpecialty(request))
                .isInstanceOf(NoSuchSpecialtyException.class)
                .hasMessage("존재하지 않는 전문분야입니다.");
    }

    @DisplayName("존재하지 않는 전문분야로 업데이트 요청 시 예외가 발생한다.")
    @Test
    void updateSpecialtyWithWrongSpecialtyProperty() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        String oldSpecialtyProperty = "프로그래밍";
        String oldDescription = "백엔드 전문가";
        String wrongSpecialtyProperty = "그래";

        var createSpecialtyRequest = CreateSpecialtyRequest.builder()
                .specialtyProperty(oldSpecialtyProperty)
                .description(oldDescription)
                .build();
        var savedSpecialty = specialtyService
                .createSpecialty(createSpecialtyRequest, savedUser.getId());

        UpdateSpecialtyRequest request = UpdateSpecialtyRequest.builder()
                .id(savedSpecialty.id())
                .property(wrongSpecialtyProperty)
                .description(oldDescription)
                .build();

        // when then
        assertThatThrownBy(() -> specialtyService.updateSpecialty(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid value: " + wrongSpecialtyProperty);
    }

    @DisplayName("전문분야 아이디로 전문분야를 삭제한다.")
    @Test
    void deleteSpecialty() {
        // given
        User user = createUser("cnsong", "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        String specialtyProperty = "프로그래밍";
        String Description = "백엔드 전문가";

        var createSpecialtyRequest = CreateSpecialtyRequest.builder()
                .specialtyProperty(specialtyProperty)
                .description(Description)
                .build();
        var savedSpecialty = specialtyService
                .createSpecialty(createSpecialtyRequest, savedUser.getId());

        // when
        specialtyService.deleteSpecialty(savedSpecialty.id());

        // then
        assertFalse(Thread.currentThread().isInterrupted());
        assertFalse(specialtyRepository.findById(savedSpecialty.id()).isPresent());
    }

    @DisplayName("존재하지 않는 전문분야 아이디로 전문분야를 삭제하려고 하면 예외가 발생한다.")
    @Test
    void deleteSpecialtyWithWrongSpecialtyId() {
        // given
        Long wrongSpecialtyId = 10L;

        // when then
        assertThatThrownBy(() -> specialtyService.deleteSpecialty(wrongSpecialtyId))
                .isInstanceOf(NoSuchSpecialtyException.class)
                .hasMessage("존재하지 않는 전문분야입니다.");
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
}