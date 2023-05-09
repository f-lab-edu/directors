package com.directors.domain.specialty;

import com.directors.IntegrationTestSupport;
import com.directors.domain.specialty.exception.NoSuchSpecialtyException;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SpecialtyRepositoryTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;

    @Autowired
    SpecialtyRepository specialtyRepository;

    @DisplayName("전문 분야 엔티티의 아이디(식별자)로 전문 분야 엔티티를 조회한다.")
    @Test
    void findById() {
        // given
        String testId = "cnsong";
        User user = createUser(testId, "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        Specialty specialty = Specialty.builder()
                .specialtyInfo(new SpecialtyInfo(SpecialtyProperty.EDUCATION, "매우 잘함."))
                .user(savedUser)
                .build();
        Specialty savedSpecialty = specialtyRepository.save(specialty);

        // when
        Specialty foundSpecialty = specialtyRepository.findById(savedSpecialty.getId())
                .orElseThrow(null);

        // then
        assertThat(foundSpecialty.getId()).isNotNull();
        assertThat(foundSpecialty).extracting("id", "SpecialtyInfo", "user")
                .contains(savedSpecialty.getId(), savedSpecialty.getSpecialtyInfo(), savedSpecialty.getUser());
    }

    @DisplayName("잘못된 아이디로 전문 분야 엔티티를 조회하면 예외가 발생한다.")
    @Test
    void findByIdWithWrongId() {
        // given
        String testId = "cnsong";
        User user = createUser(testId, "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User savedUser = userRepository.save(user);

        Specialty specialty = Specialty.builder()
                .specialtyInfo(new SpecialtyInfo(SpecialtyProperty.EDUCATION, "매우 잘함."))
                .user(savedUser)
                .build();
        Specialty savedSpecialty = specialtyRepository.save(specialty);

        // when
        assertThatThrownBy(() -> specialtyRepository.findById(savedSpecialty.getId() + 1)
                .orElseThrow(NoSuchSpecialtyException::new))
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