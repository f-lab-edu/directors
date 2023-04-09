package com.directors.domain.region;

public interface RegionApiClient {
    Address findRegionAddressByLocation(double latitude, double longitude);
}