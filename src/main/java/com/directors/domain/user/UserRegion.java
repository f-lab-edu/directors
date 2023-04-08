package com.directors.domain.user;

import com.directors.domain.region.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UserRegion {
    private String id;
    private Address address;
    private String userId;
    private Long regionId;

    public void setId(String id) {
        this.id = id;
    }
    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public static UserRegion of(Address address, String userId, Long regionId) {
        return UserRegion.builder()
                .address(address)
                .userId(userId)
                .regionId(regionId)
                .build();
    }
}
