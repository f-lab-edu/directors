package com.directors.domain.region;

import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByUserId(String userId);

    Region save(Region region);

    boolean existsByUserId(String userId);
}
