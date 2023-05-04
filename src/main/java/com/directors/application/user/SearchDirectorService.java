package com.directors.application.user;

import com.directors.application.region.RegionService;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.presentation.user.request.SearchDirectorRequest;
import com.directors.presentation.user.response.GetDirectorResponse;
import com.directors.presentation.user.response.SearchDirectorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchDirectorService {
    private final RegionService regionService;
    private final UserRepository userRepository;

    @Transactional
    public GetDirectorResponse getDirector(String directorId) {
        var director = getJoinedUserByUserId(directorId);

        return new GetDirectorResponse(director.getName(), director.getNickname(), director.getUserAddress(),
                director.getSpecialtyInfoList(), director.getScheduleStartTimes());
    }

    @Transactional
    public List<SearchDirectorResponse> searchDirector(SearchDirectorRequest request, String userId) {
        // TODO: 04.10 Option -> 자기 소개 엔티티 or VO 추가 여부 , Sorting -> 검색에 평점(높은 순, 최소), 요청된 질문 수 반영 여부
        User joinedUser = getJoinedUserByUserId(userId);

        List<Long> nearestRegionIds = regionService.getNearestRegionId(joinedUser.getId(), request.distance());

        int offset = calcOffset(request.page(), request.size());

        List<User> users = userRepository.findWithSearchConditions(nearestRegionIds, request.hasSchedule(), request.searchText(),
                request.property(), offset, request.size());

        return generateDirectors(users);
    }

    private User getJoinedUserByUserId(String userId) {
        return userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));
    }

    private int calcOffset(int page, int size) {
        return (page - 1) * size;
    }

    private List<SearchDirectorResponse> generateDirectors(List<User> users) {
        return users.stream()
                .map(SearchDirectorResponse::of)
                .collect(Collectors.toList());
    }
}
