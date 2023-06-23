package com.directors.presentation.region.response;

import com.directors.domain.region.Region;
import lombok.Builder;

@Builder
public record NearestAddressResponse(
        Long regionId,
        String fullAddress,
        String unitAddress
) {
    public static NearestAddressResponse from(Region region) {
        return NearestAddressResponse.builder()
                .regionId(region.getId())
                .fullAddress(region.getAddress().getFullAddress())
                .unitAddress(region.getAddress().getUnitAddress())
                .build();
    }
}
