package com.directors.application.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.presentation.region.response.NearestAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {
    private static final int KILOMETER = 1000;

    private final RegionRepository regionRepository;

    private final UserRepository userRepository;

    @Transactional
    public List<NearestAddressResponse> getNearestAddress(int distance, String userId) {
        return getNearestRegion(distance, getRegionFromUser(userId))
                .stream()
                .map(NearestAddressResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Long> getNearestRegionId(int distance, String userId) {
        return getNearestRegion(distance, getRegionFromUser(userId))
                .stream()
                .map(Region::getId)
                .collect(Collectors.toList());
    }

    private Region getRegionFromUser(String userId) {
        return userRepository.findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId))
                .getRegion();
    }

    private List<Region> getNearestRegion(int distance, Region region) {
        return regionRepository.findRegionWithin(region.getPoint(), distance * KILOMETER);
    }
}
