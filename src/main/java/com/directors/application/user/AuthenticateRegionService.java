package com.directors.application.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;
import com.directors.presentation.user.request.AuthenticateRegionRequest;
import com.directors.presentation.user.response.AuthenticateRegionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticateRegionService {
    private final RegionApiClient regionApiClient;
    private final RegionRepository regionRepository;
    private final UserRegionRepository userRegionRepository;

    @Transactional
    public AuthenticateRegionResponse authenticate(AuthenticateRegionRequest request, String userId) {
        Address address = regionApiClient.findRegionAddressByLocation(request.latitude(), request.longitude());
        Region regionByApi = regionRepository.findByFullAddress(address.getFullAddress())
                .orElseThrow(() -> new NoSuchElementException());

        UserRegion savedUserRegion = null;

        if (userRegionRepository.existsByUserId(userId)) {
            UserRegion userRegion = userRegionRepository.findByUserId(userId).get();
            userRegion.setAddress(regionByApi.getAddress());

            savedUserRegion = userRegionRepository.save(userRegion);
        } else {
            UserRegion newUserRegion = UserRegion.of(regionByApi.getAddress(), userId, regionByApi.getId());

            savedUserRegion = userRegionRepository.save(newUserRegion);
        }

        return new AuthenticateRegionResponse(savedUserRegion.getAddress().getFullAddress(), savedUserRegion.getAddress().getUnitAddress());
    }
}
