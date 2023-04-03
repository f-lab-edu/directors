package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InmemoryRegionRepository implements RegionRepository {
    Map<String, Region> regionMap = new HashMap<>();

    @Override
    public Optional<Region> findByUserId(String userId) {
        return Optional.ofNullable(regionMap.get(userId));
    }

    @Override
    public void save(Region region) {
        regionMap.put(region.getId(), region);
    }
}
