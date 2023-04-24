package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryAdapter implements RegionRepository {
    private final JpaRegionRepository regionRepository;

    @Override
    public Optional<Region> findByFullAddress(String fullAddress) {
        return regionRepository.findByAddressFullAddress(fullAddress);
    }

    @Override
    public Optional<Region> findById(Long regionId) {
        return regionRepository.findById(regionId);
    }

    @Override
    public Region save(Region region) {
        return regionRepository.save(region);
    }

    @Override
    public void saveAll(List<Region> regions) {
        regionRepository.saveAll(regions);
    }

    @Override
    public Long count() {
        return regionRepository.count();
    }

    @Override
    public List<Region> findRegionWithin(Point point, double distance) {
        return regionRepository.findRegionByPointDistanceLessThan(point, distance);
    }
}
