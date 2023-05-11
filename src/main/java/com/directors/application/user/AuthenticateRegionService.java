package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.region.exception.RegionNotFoundException;
import com.directors.domain.user.User;
import com.directors.domain.user.UserRepository;
import com.directors.domain.user.UserStatus;
import com.directors.domain.user.exception.NoSuchUserException;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.response.AuthenticateRegionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateRegionService {
    private final RegionApiClient regionApiClient;
    private final RegionRepository regionRepository;
    private final UserRepository userRepository;

    @Transactional
    public AuthenticateRegionResponse authenticate(AuthenticateRegionRequest request, String userId) {
        var addressByApi = regionApiClient.findRegionAddressByLocation(request.latitude(), request.longitude());

        User user = getUser(userId);
        user.authenticateRegion(getRegion(addressByApi));

        return new AuthenticateRegionResponse(user.getUserAddress().getFullAddress(), user.getUserAddress().getUnitAddress());
    }

    private Region getRegion(Address addressByApi) {
        return regionRepository
                .findByFullAddress(addressByApi.getFullAddress())
                .orElseThrow(() -> new RegionNotFoundException(addressByApi.getFullAddress()));
    }

    private User getUser(String userId) {
        return userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));
    }
}
