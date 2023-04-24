package com.directors.domain.region;

import org.locationtech.jts.geom.Point;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByFullAddress(String fullAddress);

    Optional<Region> findById(Long regionId);

    Region save(Region region);

    void saveAll(List<Region> regions);

    List<Region> findRegionWithin(Point point, double distance);

    Long count();
}
