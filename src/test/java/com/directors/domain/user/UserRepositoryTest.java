package com.directors.domain.user;

import com.directors.IntegrationTestSupport;
import com.directors.domain.user.exception.NoSuchUserException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 가입 상태인 유저 아이디로 유저를 조회한다.")
    @Test
    void findJoindedUserById() {
        // given
        String testId = "cnsong";
        User user = createUser(testId, "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        userRepository.save(user);

        // when
        User findUser = userRepository.findByIdAndUserStatus(testId, UserStatus.JOINED)
                .orElseThrow(null);

        // then
        assertThat(findUser.getId()).isNotNull().isEqualTo(testId);
        assertThat(findUser).extracting("password", "email", "phoneNumber", "name", "nickname")
                .contains(user.getPassword(), user.getEmail(), user.getPhoneNumber(), user.getName(), user.getNickname());
    }

    @DisplayName("가입되지 않은 유저 아이디로 유저를 조회하면 예외가 발생한다.")
    @Test
    void findUnregisteredIdById() {
        // given
        String testId = "cnsong";
        User user = createUser(testId, "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        userRepository.save(user);

        String unregisteredId = "tnsong";

        // when // then
        assertThatThrownBy(() -> userRepository.findByIdAndUserStatus(unregisteredId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(unregisteredId)))
                .isInstanceOf(NoSuchUserException.class)
                .hasMessage("존재하지 않는 유저 Id입니다.");
    }

    @DisplayName("탈퇴한 유저 아이디를 가입한 유저로 조회하면 예외가 발생한다.")
    @Test
    void findWithdrawnIdById() {
        // given
        String testId = "cnsong";
        User user = createUser(testId, "1234567890", "thddmstjrwkd@naver.com", "01077021045", "송은석", "cnsong");
        User saveUser = userRepository.save(user);

        LocalDateTime now = LocalDateTime.now();
        saveUser.withdrawal(now);

        // when // then
        assertThatThrownBy(() -> userRepository.findByIdAndUserStatus(testId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(testId)))
                .isInstanceOf(NoSuchUserException.class)
                .hasMessage("존재하지 않는 유저 Id입니다.");
    }

    // TODO: 05.05 findWithSearchConditions 는 추후 테스트

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