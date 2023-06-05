package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RegionRepositoryAdapter implements RegionRepository {
    private final JpaRegionRepository regionRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    public List<Region> findRegionWithin(double x, double y, double distance) {
        String nativeQuery = "SELECT * FROM region r WHERE ST_Contains(ST_Buffer(ST_GeomFromText(?, 5179), ?), point)";
        Query query = entityManager.createNativeQuery(nativeQuery, Region.class);

        query.setParameter(1, String.format("POINT(%f %f)", x, y));
        query.setParameter(2, distance);

        return query.getResultList();
    }
}