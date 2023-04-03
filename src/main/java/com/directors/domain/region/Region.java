package com.directors.domain.region;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Region {
    private String id;
    private String fullAddress;
    private String unitAddress;
    private String userId;

    public void updateAddressInfo(String fullAddress, String unitAddress) {
        this.fullAddress = fullAddress;
        this.unitAddress = unitAddress;
    }
}
