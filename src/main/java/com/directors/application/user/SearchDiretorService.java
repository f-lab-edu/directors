package com.directors.application.user;

import com.directors.application.region.RegionService;
import com.directors.domain.region.Address;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.specialty.SpecialtyProperty;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.domain.user.*;
import com.directors.infrastructure.exception.api.NotFoundException;
import com.directors.presentation.user.request.SearchDirectorRequest;
import com.directors.presentation.user.response.GetDirectorResponse;
import com.directors.presentation.user.response.SearchDirectorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchDiretorService {
    private final RegionService regionService;
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;
    private final ScheduleRepository scheduleRepository;
    private final SpecialtyRepository specialtyRepository;

    public GetDirectorResponse getDirector(String directorId) {
        User director = getDirectByDirectorId(directorId);
        Address address = getAddressByDirectorId(director.getUserId());

        List<SpecialtyInfo> specialtyInfoList = getSpecialtyInfoListByDirectorId(director.getUserId());
        List<LocalDateTime> startTimeList = getScheduleStartTimesByDirectorId(director.getUserId());

        return new GetDirectorResponse(director.getName(), director.getNickname(), address, specialtyInfoList, startTimeList);
    }

    public List<SearchDirectorResponse> searchDirector(SearchDirectorRequest request, String userId) {
        // TODO: 04.10 Option -> 자기 소개 엔티티 or VO 추가 여부 , Sorting -> 검색에 평점(높은 순, 최소), 요청된 질문 수 반영 여부
        List<String> userIds = getNearestDirectorIds(userId, request.distance());

        userIds = filterUserIdByText(userIds, request.searchText());
        userIds = filterUserIdBySpecialtyProperty(userIds, request.specialtyProperty());
        userIds = filterUserIdBySchedule(userIds, request.hasSchedule());

        return paging(request.size(), request.page(), generateDirector(userIds));
    }

    private User getDirectByDirectorId(String directorId) {
        return userRepository
                .findByIdAndUserStatus(directorId, UserStatus.JOINED)
                .orElseThrow(NotFoundException::new);
    }

    private Address getAddressByDirectorId(String directorId) {
        return userRegionRepository
                .findByUserId(directorId)
                .orElseThrow(NotFoundException::new)
                .getAddress();
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
        List<Address> nearestAddress = regionService.getNearestAddress(userId, distance);

        List<UserRegion> userRegion = new ArrayList<>();

        for (Address address : nearestAddress) {
            userRegion.addAll(userRegionRepository.findByFullAddress(address.fullAddress()));
        }

        if (userRegion.size() == 0) {
            throw new NotFoundException();
        }

        return userRegion.stream()
                .map(UserRegion::getUserId)
                .collect(Collectors.toList());
    }

    private List<String> filterUserIdByText(List<String> userIds, String searchText) {
        if (searchText == null) {
            return userIds;
        }

        Set<String> filteredIds = new HashSet<>();

        for (String id : userIds) {
            addMatchingUserIds(searchText, filteredIds, id);
            addMatchingSpecialtyUserIds(searchText, filteredIds, id);
        }

        return new ArrayList<>(filteredIds);
    }

    private void addMatchingUserIds(String searchText, Set<String> filteredIds, String id) {
        User user = userRepository.findByIdAndUserStatus(id, UserStatus.JOINED).orElseThrow();

        if (user.getUserId().contains(searchText) || user.getNickname().contains(searchText)) {
            filteredIds.add(id);
        }
    }

    private void addMatchingSpecialtyUserIds(String searchText, Set<String> filteredIds, String id) {
        specialtyRepository
                .findByUserId(id)
                .stream()
                .filter(sp -> sp.getSpecialtyInfo().description().contains(searchText))
                .findFirst().ifPresent(sp -> filteredIds.add(id));
    }

    private List<String> filterUserIdBySpecialtyProperty(List<String> userIds, SpecialtyProperty specialtyProperty) {
        List<String> filteredIds = new ArrayList<>();

        for (String id : userIds) {
            boolean hasSpecialtyPropertyId = specialtyRepository
                    .findByUserId(id)
                    .stream()
                    .anyMatch(specialty -> specialty.getSpecialtyInfo().property().equals(specialtyProperty));

            if (hasSpecialtyPropertyId) {
                filteredIds.add(id);
            }
        }

        return filteredIds;
    }

    private List<String> filterUserIdBySchedule(List<String> userIds, boolean hasSchedule) {
        if (!hasSchedule) {
            return userIds;
        }

        List<String> filteredIds = new ArrayList<>();

        for (String id : userIds) {
            boolean hasScheduleId = scheduleRepository
                    .existsByUserIdAndScheduleStatus(id, ScheduleStatus.OPENED);
            if (hasScheduleId) {
                filteredIds.add(id);
            }
        }

        return filteredIds;
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

            responses.add(new SearchDirectorResponse(user.getUserId(), user.getName(), specialtyProperties));
        }

        return responses;
    }
}
