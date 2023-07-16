package com.directors.application.user;

import com.directors.application.schedule.ScheduleService;
import com.directors.domain.region.Address;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.domain.user.User;
import com.directors.domain.user.UserStatus;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.request.SearchDirectorRequest;
import com.directors.presentation.user.response.SearchDirectorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class FindWithSearchConditionsTest extends UserTestSupport {

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
        String testUserId = "testIdForSearch10";

        String sampleTestId = "testIdForSearch";
        String testNickname = "testNickname";

        when(regionApiClient.findRegionAddressByLocation(961487, 1949977))
                .thenReturn(new Address("서울특별시 성동구 송정동", "송정동"));
        when(regionApiClient.findRegionAddressByLocation(963576.244004357955, 1951579.27269738005)) // 위경도 찾기
                .thenReturn(new Address("서울특별시 광진구 중곡동", "중곡동"));
        AuthenticateRegionRequest songjeongdong = createAuthenticateRegionRequest(961487, 1949977);
        AuthenticateRegionRequest joonggokdong = createAuthenticateRegionRequest(963576.244004357955, 1951579.27269738005);

        // 유저 1~100 save
        for (int i = 1; i <= 100; i++) {
            String userTestId = sampleTestId + (i + "");
            User user = createUser(userTestId, "1234567890", "thddmstjrwkd@naver.com",
                    "01077021045", "송은석", testNickname + (i + ""));
            userRepository.save(user);

            if (i >= 10 && i < 40) { // 유저 10~39 -> 지역을 서울특별시 성동구 송정동 할당
                authenticateRegionService.authenticate(songjeongdong, userTestId);
            }
            if (i >= 40 && i < 70) { // 유저 40~79 -> 지역을 서울특별시 광진구 중곡동 할당
                authenticateRegionService.authenticate(joonggokdong, userTestId);
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
            // 유저 40~59 -> specialty로 PROGRAMMING 할당
            specialty = createSpecialty(user, SpecialtyProperty.PROGRAMMING);
            if (i >= 40 && i < 60) {
                specialtyRepository.save(specialty);
            }

            if (i % 5 == 0) { // 5 단위로 스케줄 할당
                scheduleService.open(userTestId, List.of(LocalDateTime.of(2030, 5, 11, 12, 0)));
            }
        }

        return List.of(
                DynamicTest.dynamicTest("기본 조건으로 디렉터를 조회한다.", () -> {
                    // given
                    var request = createSearchDirectorRequest(1, 100, null, 1, null, false);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(30);
                }),
                DynamicTest.dynamicTest("유저 근처 거리를 기준으로 디렉터를 조회한다.", () -> {
                    // given
                    int givenDistance = 3;
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 100, null, givenDistance, null, false);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(60);
                }),
                DynamicTest.dynamicTest("specialty가 교육인 디렉터를 조회한다.", () -> {
                    // given
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 100, null, 1, "교육", false);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(10);
                    assertThat(responses).extracting("directorId")
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
                DynamicTest.dynamicTest("specialty가 예술인 디렉터를 조회한다.", () -> {
                    // given
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 100, null, 3, "예술", false);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);
                    // then
                    assertThat(responses).hasSize(10);
                    assertThat(responses).extracting("directorId")
                            .contains(
                                    "testIdForSearch39",
                                    "testIdForSearch38",
                                    "testIdForSearch37",
                                    "testIdForSearch36",
                                    "testIdForSearch35",
                                    "testIdForSearch34",
                                    "testIdForSearch33",
                                    "testIdForSearch32",
                                    "testIdForSearch31",
                                    "testIdForSearch30"
                            );
                }),
                DynamicTest.dynamicTest("searchText를 통해 디렉터를 조회한다.", () -> {
                    // given
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 100, "testNickname1", 1, null, false);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(10);
                    assertThat(responses).extracting("directorId")
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
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 100, null, 1, null, true);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(6);
                    assertThat(responses).extracting("directorId")
                            .contains(
                                    "testIdForSearch35",
                                    "testIdForSearch30",
                                    "testIdForSearch25",
                                    "testIdForSearch20",
                                    "testIdForSearch15",
                                    "testIdForSearch10"
                            );
                }),
                DynamicTest.dynamicTest("복합 조건 검색: text + distance + SpecialtyProperty + hasSchedule", () -> {
                    // given
                    SearchDirectorRequest request = createSearchDirectorRequest(1, 10, "testNickname4", 3, "프로그래밍", true);

                    // when
                    List<SearchDirectorResponse> responses = searchDirectorService.searchDirector(request, testUserId);

                    // then
                    assertThat(responses).hasSize(2);
                    assertThat(responses).extracting("directorId")
                            .contains(
                                    "testIdForSearch45",
                                    "testIdForSearch40"
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

    private static SearchDirectorRequest createSearchDirectorRequest(int page, int size, String searchText, int distance, String property, boolean hasSchedule) {
        return SearchDirectorRequest.builder()
                .page(page)
                .size(size)
                .searchText(searchText)
                .distance(distance)
                .property(property)
                .hasSchedule(hasSchedule)
                .build();
    }

    private static Specialty createSpecialty(User user, SpecialtyProperty specialtyProperty) {
        return Specialty.builder()
                .property(specialtyProperty)
                .description( "매우 잘함.")
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
