package com.directors.application.user;

import com.directors.application.region.RegionService;
import com.directors.domain.region.Address;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.user.*;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.domain.user.exception.UserRegionNotFoundException;
import com.directors.presentation.user.request.SearchDirectorRequest;
import com.directors.presentation.user.response.GetDirectorResponse;
import com.directors.presentation.user.response.SearchDirectorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchDirectorService {
    private final RegionService regionService;
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;
    private final ScheduleRepository scheduleRepository;

    public GetDirectorResponse getDirector(String directorId) {
        var director = getDirectorByDirectorId(directorId);
        // TODO: 04.25 Schedule 도메인 Jpa 적용 후 변경 필요. -> User 클래스 내에 다 있도록.
        var startTimeList = getScheduleStartTimesByDirectorId(director.getId());

        return new GetDirectorResponse(director.getName(), director.getNickname(), director.getUserAddress(), director.getSpecialtyInfoList(), startTimeList);
    }

    public List<SearchDirectorResponse> searchDirector(SearchDirectorRequest request, String userId) {
        // TODO: 04.10 Option -> 자기 소개 엔티티 or VO 추가 여부 , Sorting -> 검색에 평점(높은 순, 최소), 요청된 질문 수 반영 여부
        var users = getNearestDirectors(userId, request.distance());

        // TODO: 04.25 Schedule 도메인 Jpa 적용 후 변경 필요. -> User 클래스 내에 다 있도록.
        users = filterUserIdBySchedule(users, request.hasSchedule());
        users = users.stream()
                .filter(user -> user.hasText(request.searchText()) || user.hasProperty(request.property()))
                .collect(Collectors.toList());
        return paging(request.size(), request.page(), generateDirectors(users));
    }

    private User getDirectorByDirectorId(String directorId) {
        return userRepository
                .findByIdAndUserStatus(directorId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(directorId));
    }

    private List<LocalDateTime> getScheduleStartTimesByDirectorId(String directorId) {
        return scheduleRepository
                .findByUserIdAndScheduleStatus(directorId, ScheduleStatus.OPENED)
                .stream()
                .map(Schedule::getStartTime)
                .collect(Collectors.toList());
    }

    private List<String> getNearestDirectorIds(String userId, int distance) {
        var nearestAddress = regionService.getNearestAddress(userId, distance);

        List<UserRegion> userRegionList = new ArrayList<>();

        for (Address address : nearestAddress) {
            var userRegion = userRegionRepository
                    .findByFullAddress(address.getFullAddress());
            userRegionList.addAll(userRegion);
        }

        if (userRegionList.size() == 0) {
            throw new UserRegionNotFoundException(null);
        }
        return userRegionList.stream()
                .map(userRegion -> userRegion.getUser())
                .collect(Collectors.toList());
    }

    private List<User> filterUserIdBySchedule(List<User> users, boolean hasSchedule) {
        if (!hasSchedule) {
            return users;
        }
        return users.stream()
                .filter(user -> scheduleRepository.existsByUserIdAndScheduleStatus(user.getId(), ScheduleStatus.OPENED))
                .collect(Collectors.toList());
    }

    private static List<SearchDirectorResponse> paging(int size, int page, List<SearchDirectorResponse> responses) {
        int totalSize = responses.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalSize);
        return responses.subList(fromIndex, toIndex);
    }

    private List<SearchDirectorResponse> generateDirectors(List<User> users) {
        return users.stream()
                .map(user -> new SearchDirectorResponse(user.getId(), user.getName(), user.getSpecialtyInfoList()))
                .collect(Collectors.toList());
    }
}
