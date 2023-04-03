package com.directors.application.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionApiClient;
import com.directors.domain.region.RegionRepository;
import com.directors.presentation.region.request.AuthenticateRegionRequest;
import com.directors.presentation.region.response.AuthenticateRegionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    private final RegionApiClient regionApiClient;

    @Transactional
    public AuthenticateRegionResponse authenticate(AuthenticateRegionRequest request, String userId) {
        Region regionByApi = regionApiClient.findRegionNamesByLocation(request.latitude(), request.longitude());

        Region savedRegion = null;

        if (regionRepository.existsByUserId(userId)) {
            Region regionByUserId = regionRepository.findByUserId(userId).get();
            regionByUserId.updateAddressInfo(regionByApi.getFullAddress(), regionByApi.getUnitAddress());

            savedRegion = regionRepository.save(regionByUserId);
        } else {
            regionByApi.setUserId(userId);

            savedRegion = regionRepository.save(regionByApi);
        }

        return new AuthenticateRegionResponse(savedRegion.getFullAddress(), savedRegion.getUnitAddress());
    }
}
