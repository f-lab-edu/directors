package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InmemoryRegionRepository implements RegionRepository {
    Map<String, Region> regionMap = new HashMap<>();

    private long nextId = 1; // 다음 id 값

    @Override
    public Optional<Region> findByUserId(String userId) {
        return Optional.ofNullable(regionMap.get(userId));
    }

    @Override
    public Region save(Region region) {
        if (region.getId() == null) {
            region.setId(String.valueOf(nextId++));
        }
        regionMap.put(region.getId(), region);

        return region;
    }

    @Override
    public boolean existsByUserId(String userId) {
        return regionMap.entrySet()
                .stream()
                .anyMatch(r -> r.getValue().getUserId().equals(userId));
    }
}
