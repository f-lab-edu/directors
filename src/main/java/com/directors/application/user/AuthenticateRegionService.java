package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.region.exception.RegionNotFoundException;
import com.directors.domain.user.*;
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
    private final UserRegionRepository userRegionRepository;
    private final UserRepository userRepository;

    @Transactional
    public AuthenticateRegionResponse authenticate(AuthenticateRegionRequest request, String userId) {
        var addressByApi = regionApiClient.findRegionAddressByLocation(request.latitude(), request.longitude());

        var region = regionRepository.findByFullAddress(addressByApi.getFullAddress())
                .orElseThrow(() -> new RegionNotFoundException(addressByApi.getFullAddress()));

        var userAddress = updateUserRegionAddress(userId, region);

        return new AuthenticateRegionResponse(userAddress.getFullAddress(), userAddress.getUnitAddress());
    }

    private Address updateUserRegionAddress(String userId, Region region) {
        if (userRegionRepository.existsByUserId(userId)) {
            return updateExistUserRegion(userId, region).getAddress();
        }
        return saveUserRegion(userId, region).getAddress();
    }

    private UserRegion updateExistUserRegion(String userId, Region region) {
        var userRegion = userRegionRepository.findByUserId(userId).get();

        userRegion.updateRegionInfo(region.getAddress(), region);

        return userRegion;
    }

    private UserRegion saveUserRegion(String userId, Region region) {
        User user = userRepository
                .findByIdAndUserStatus(userId, UserStatus.JOINED)
                .orElseThrow(() -> new NoSuchUserException(userId));
        var newUserRegion = UserRegion.of(region.getAddress(), user, region);

        return userRegionRepository.save(newUserRegion);
    }
}
