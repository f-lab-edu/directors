package com.directors.domain.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class Region {
    private String id;
    private String fullAddress;
    private String unitAddress;
    private String userId;

    public void setId(String id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void updateAddressInfo(String fullAddress, String unitAddress) {
        this.fullAddress = fullAddress;
        this.unitAddress = unitAddress;
    }
}
