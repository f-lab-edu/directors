package com.directors.application.user;

import com.directors.application.region.RegionService;
import com.directors.domain.region.Address;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.api.NotFoundException;
import com.directors.infrastructure.jpa.specialty.JpaSpecialtyRepository;
import com.directors.infrastructure.jpa.user.JpaUserRegionRepository;
import com.directors.infrastructure.jpa.user.JpaUserRepository;
import com.directors.presentation.user.request.SearchDirectorRequest;
import com.directors.presentation.user.response.GetDirectorResponse;
import com.directors.presentation.user.response.SearchDirectorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchDirectorService {
    private final RegionService regionService;
    private final JpaUserRepository userRepository;
    private final JpaUserRegionRepository userRegionRepository;
    private final ScheduleRepository scheduleRepository;
    private final JpaSpecialtyRepository specialtyRepository;

    public GetDirectorResponse getDirector(String directorId) {
        var director = getDirectByDirectorId(directorId);

        var address = getAddressByDirectorId(director.getId()).orElse(null);

        var specialtyInfoList = getSpecialtyInfoListByDirectorId(director.getId());
        var startTimeList = getScheduleStartTimesByDirectorId(director.getId());

        return new GetDirectorResponse(director.getName(), director.getNickname(), address, specialtyInfoList, startTimeList);
    }

    public List<SearchDirectorResponse> searchDirector(SearchDirectorRequest request, String userId) {
        // TODO: 04.10 Option -> 자기 소개 엔티티 or VO 추가 여부 , Sorting -> 검색에 평점(높은 순, 최소), 요청된 질문 수 반영 여부
        var userIds = getNearestDirectorIds(userId, request.distance());

        userIds = filterUserIdByText(userIds, request.searchText());
        userIds = filterUserIdBySpecialtyProperty(userIds, request.property());
        userIds = filterUserIdBySchedule(userIds, request.hasSchedule());

        return paging(request.size(), request.page(), generateDirector(userIds));
    }

    private User getDirectByDirectorId(String directorId) {
        return userRepository
                .findByIdAndUserStatus(directorId, UserStatus.JOINED)
                .orElseThrow(NotFoundException::new);
    }

    private Optional<Address> getAddressByDirectorId(String directorId) {
        return userRegionRepository
                .findByUserId(directorId)
                .map(UserRegion::getAddress);
    }

    private List<SpecialtyInfo> getSpecialtyInfoListByDirectorId(String directorId) {
        return specialtyRepository
                .findByUserId(directorId)
                .stream()
                .map(Specialty::getSpecialtyInfo)
                .collect(Collectors.toList());
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
                    .findByAddressFullAddress(address.getFullAddress());
            userRegionList.addAll(userRegion);
        }

        if (userRegionList.size() == 0) {
            throw new NotFoundException();
        }

        return userRegionList.stream()
                .map(userRegion -> userRegion.getUser().getId())
                .collect(Collectors.toList());
    }

    private List<String> filterUserIdByText(List<String> userIds, String searchText) {
        if (searchText == null) {
            return userIds;
        }
        return userIds.stream()
                .filter(id -> isMatchingUserExistsWithText(searchText, id) || isMatchingSpecialtyWithText(searchText, id))
                .collect(Collectors.toList());
    }

    private boolean isMatchingUserExistsWithText(String searchText, String id) {
        return userRepository
                .findByIdAndUserStatus(id, UserStatus.JOINED)
                .filter(user -> user.getId().contains(searchText) || user.getNickname().contains(searchText))
                .isPresent();
    }

    private boolean isMatchingSpecialtyWithText(String searchText, String id) {
        return specialtyRepository
                .findByUserId(id)
                .stream()
                .anyMatch(sp -> sp.getSpecialtyInfo().getDescription().contains(searchText));
    }

    private List<String> filterUserIdBySpecialtyProperty(List<String> userIds, String specialtyProperty) {
        if (specialtyProperty == null) {
            return userIds;
        }
        return userIds.stream()
                .filter(id -> hasSpecialtyProperty(id, specialtyProperty))
                .collect(Collectors.toList());
    }

    private boolean hasSpecialtyProperty(String id, String specialtyProperty) {
        return specialtyRepository.findByUserId(id).stream()
                .anyMatch(specialty ->
                        specialty.getSpecialtyInfo().getProperty().getValue().equals(specialtyProperty));
    }

    private List<String> filterUserIdBySchedule(List<String> userIds, boolean hasSchedule) {
        if (!hasSchedule) {
            return userIds;
        }
        return userIds.stream()
                .filter(id -> scheduleRepository.existsByUserIdAndScheduleStatus(id, ScheduleStatus.OPENED))
                .collect(Collectors.toList());
    }

    private static List<SearchDirectorResponse> paging(int size, int page, List<SearchDirectorResponse> responses) {
        int totalSize = responses.size();
        int fromIndex = (page - 1) * size;
        int toIndex = Math.min(fromIndex + size, totalSize);
        return responses.subList(fromIndex, toIndex);
    }

    private List<SearchDirectorResponse> generateDirector(List<String> userIds) {
        List<SearchDirectorResponse> responses = new ArrayList<>();

        for (String id : userIds) {
            User user = userRepository.findByIdAndUserStatus(id, UserStatus.JOINED).orElseThrow();
            List<SpecialtyInfo> specialtyProperties = specialtyRepository
                    .findByUserId(id)
                    .stream()
                    .map(Specialty::getSpecialtyInfo)
                    .collect(Collectors.toList());

            responses.add(new SearchDirectorResponse(user.getId(), user.getName(), specialtyProperties));
        }

        return responses;
    }
}
