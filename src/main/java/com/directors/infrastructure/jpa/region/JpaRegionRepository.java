package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaRegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByAddressFullAddress(String fullAddress);
}
