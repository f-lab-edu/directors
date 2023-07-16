package com.directors.domain.user;

import com.directors.application.schedule.ScheduleService;
import com.directors.application.user.SearchDirectorService;
import com.directors.application.user.UserTestSupport;
import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FindWithSearchConditionsRepositoryTest extends UserTestSupport {

    @MockBean
    RegionApiClient regionApiClient;

    @Autowired
    SpecialtyRepository specialtyRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    SearchDirectorService searchDirectorService;

    @DisplayName("검색 시나리오")
    @TestFactory
    Collection<DynamicTest> findWithSearchConditionsDynamicTest() {
        // common given
        List<Long> givenRegionIds = new ArrayList<>();
        int givenOffset = 0;
        int givenLimit = 100;

        String testUserId = "testIdForSearch10";

        String sampleTestId = "testIdForSearch";
        String testNickname = "testNickname";

        when(regionApiClient.findRegionAddressByLocation(961487, 1949977))
                .thenReturn(new Address("서울특별시 성동구 송정동", "송정동"));
        AuthenticateRegionRequest songjeongdong = createAuthenticateRegionRequest(961487, 1949977);

        // 유저 1~100 save
        for (int i = 1; i <= 100; i++) {
            String userTestId = sampleTestId + (i + "");
            User user = createUser(userTestId, "1234567890", "thddmstjrwkd@naver.com",
                    "01077021045", "송은석", testNickname + (i + ""));
            userRepository.save(user);

            if (i >= 10 && i < 40) { // 유저 10~39 -> 지역을 서울특별시 성동구 송정동 할당
                authenticateRegionService.authenticate(songjeongdong, userTestId);
            }

            // 유저 20~29 -> specialty로 EDUCATION 할당
            Specialty specialty = createSpecialty(user, SpecialtyProperty.EDUCATION);
            if (i >= 20 && i < 30) {
                specialtyRepository.save(specialty);
            }
            // 유저 30~39 -> specialty로 ART 할당
            specialty = createSpecialty(user, SpecialtyProperty.ART);
            if (i >= 30 && i < 40) {
                specialtyRepository.save(specialty);
            }

            if (i % 5 == 0) { // 5 단위로 스케줄 할당
                scheduleService.open(userTestId, List.of(LocalDateTime.of(2030, 5, 11, 12, 0)));
            }
        }

        User user = userRepository.findById(testUserId).orElseThrow(null);
        givenRegionIds.add(user.getRegion().getId());

        return List.of(
                DynamicTest.dynamicTest("기본 조건으로 디렉터를 조회한다.", () -> {
                    // given when
                    List<User> users = userRepository
                            .findWithSearchConditions(givenRegionIds, false, null, null, givenOffset, givenLimit);

                    // then
                    assertThat(users).hasSize(30);
                }),
                DynamicTest.dynamicTest("specialty가 교육인 디렉터를 조회한다.", () -> {
                    // given
                    SpecialtyProperty givenProperty = SpecialtyProperty.EDUCATION;

                    // when
                    List<User> users = userRepository
                            .findWithSearchConditions(givenRegionIds, false, null, givenProperty, givenOffset, givenLimit);

                    // then
                    assertThat(users).hasSize(10);
                    assertThat(users).extracting("id")
                            .contains(
                                    "testIdForSearch29",
                                    "testIdForSearch28",
                                    "testIdForSearch27",
                                    "testIdForSearch26",
                                    "testIdForSearch25",
                                    "testIdForSearch24",
                                    "testIdForSearch23",
                                    "testIdForSearch22",
                                    "testIdForSearch21",
                                    "testIdForSearch20"
                            );
                }),
                DynamicTest.dynamicTest("searchText를 통해 디렉터를 조회한다.", () -> {
                    // given
                    String givenSearchText = "testNickname1";

                    // when
                    List<User> users = userRepository
                            .findWithSearchConditions(givenRegionIds, false, givenSearchText, null, givenOffset, givenLimit);

                    // then
                    assertThat(users).hasSize(10);
                    assertThat(users).extracting("id")
                            .contains(
                                    "testIdForSearch19",
                                    "testIdForSearch18",
                                    "testIdForSearch17",
                                    "testIdForSearch16",
                                    "testIdForSearch15",
                                    "testIdForSearch14",
                                    "testIdForSearch13",
                                    "testIdForSearch12",
                                    "testIdForSearch11",
                                    "testIdForSearch10"
                            );
                }),
                DynamicTest.dynamicTest("스케줄 유무를 통해 디렉터를 조회한다.", () -> {
                    // given
                    boolean givenHasSchedule = true;

                    // when
                    List<User> users = userRepository
                            .findWithSearchConditions(givenRegionIds, givenHasSchedule, null, null, givenOffset, givenLimit);

                    // then
                    assertThat(users).hasSize(6);
                    assertThat(users).extracting("id")
                            .contains(
                                    "testIdForSearch35",
                                    "testIdForSearch30",
                                    "testIdForSearch25",
                                    "testIdForSearch20",
                                    "testIdForSearch15",
                                    "testIdForSearch10"
                            );
                }),
                DynamicTest.dynamicTest("복합 조건 검색: hasSchedule + text + SpecialtyProperty", () -> {
                    // given
                    boolean givenHasSchedule = true;
                    String givenSearchText = "testNickname3";
                    SpecialtyProperty givenProperty = SpecialtyProperty.ART;

                    // when
                    List<User> users = userRepository
                            .findWithSearchConditions(givenRegionIds, givenHasSchedule, givenSearchText, givenProperty, givenOffset, givenLimit);

                    // then
                    assertThat(users).hasSize(2);
                    assertThat(users).extracting("id")
                            .contains(
                                    "testIdForSearch35",
                                    "testIdForSearch30"
                            );
                })
        );
    }

    private static AuthenticateRegionRequest createAuthenticateRegionRequest(double latitude, double longitude) {
        return AuthenticateRegionRequest.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }

    private static Specialty createSpecialty(User user, SpecialtyProperty property) {
        return Specialty.builder()
                .property(property)
                .description("매우 잘함.")
                .user(user)
                .build();
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
