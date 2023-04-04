package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InmemoryRegionRepository implements RegionRepository {
    Map<Long, Region> regionMap = new HashMap<>();

    private long nextId = 1;

    @Override
    public Optional<Region> findByFullAddress(String fullAddress) {
        return regionMap.values()
                .stream()
                .filter(r -> r.getAddress().getFullAddress().equals(fullAddress))
                .findFirst();
    }

    @Override
    public Region findByRegionId(Long regionId) {
        return regionMap.get(regionId);
    }

    @Override
    public Region save(Region region) {
        if (region.getId() == null) {
            region.setId(nextId++);
        }
        regionMap.put(region.getId(), region);

        return region;
    }

    public void saveAll(List<Region> regions) {
        for (Region region : regions) {
            if (region.getId() == null) {
                region.setId(nextId++);
            }
            regionMap.put(region.getId(), region);
        }
    }
}
