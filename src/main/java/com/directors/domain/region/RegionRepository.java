package com.directors.domain.region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByFullAddress(String fullAddress);

    Optional<Region> findByRegionId(Long regionId);

    Region save(Region region);

    void saveAll(List<Region> regions);

    List<Region> findRegionWithin(Region region, double distance);
}
