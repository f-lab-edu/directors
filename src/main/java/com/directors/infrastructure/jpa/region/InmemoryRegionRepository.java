package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import org.locationtech.jts.geom.Point;

import java.util.*;

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
    public Optional<Region> findById(Long regionId) {
        return Optional.ofNullable(regionMap.get(regionId));
    }

    @Override
    public Region save(Region region) {
        if (region.getId() == null) {
            region.setId(nextId++);
        }
        regionMap.put(region.getId(), region);

        return region;
    }

    @Override
    public void saveAll(List<Region> regions) {
        for (Region region : regions) {
            if (region.getId() == null) {
                region.setId(nextId++);
            }
            regionMap.put(region.getId(), region);
        }
    }

    @Override
    public List<Region> findRegionWithin(Point point, double distance) {
        List<Region> result = new ArrayList<>();

        for (Region oneRegion : regionMap.values()) {
            if (oneRegion.getPoint().distance(point) <= distance) {
                result.add(oneRegion);
            }
        }

        return result;
    }

    @Override
    public Long count() {
        return Long.parseLong(String.valueOf(regionMap.size()));
    }
}
