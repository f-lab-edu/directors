package com.directors.domain.region;

public interface RegionApiClient {
    Region findRegionNamesByLocation(double latitude, double longitude);
}