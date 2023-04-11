package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.schedule.ScheduleRepository;
import com.directors.domain.schedule.ScheduleStatus;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyRepository;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRegionRepository;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.infrastructure.exception.user.NoSuchUserException;
import com.directors.presentation.user.response.GetDirectorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final UserRegionRepository userRegionRepository;
    private final ScheduleRepository scheduleRepository;
    private final SpecialtyRepository specialtyRepository;

    public GetDirectorResponse getDirector(String directorId) {
        User user = userRepository
                .findUserByIdAndUserStatus(directorId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(directorId));

        String name = user.getName();
        String nickname = user.getNickname();

        Address address = userRegionRepository
                .findByUserId(user.getUserId())
                .orElseThrow()
                .getAddress();

        List<Specialty> specialList = specialtyRepository.findByUserId(user.getUserId());
        List<Schedule> scheduleList = scheduleRepository.findByUserIdAndScheduleStatus(user.getUserId(), ScheduleStatus.OPENED);
        // + 완료 질문, 요청 질문 수

        return new GetDirectorResponse(name, nickname, address, specialList, scheduleList);
    }
}
