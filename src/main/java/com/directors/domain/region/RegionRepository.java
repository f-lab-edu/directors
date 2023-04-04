package com.directors.domain.region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByFullAddress(String fullAddress);

    Region findByRegionId(Long regionId);

    Region save(Region region);

    void saveAll(List<Region> regions);
}
