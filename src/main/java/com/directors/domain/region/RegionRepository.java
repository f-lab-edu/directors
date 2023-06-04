package com.directors.domain.region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByFullAddress(String fullAddress);

    Optional<Region> findById(Long regionId);

    Region save(Region region);

    void saveAll(List<Region> regions);

    List<Region> findRegionWithin(double x, double y, double distance);

    Long count();
}
