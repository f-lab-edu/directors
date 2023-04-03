package com.directors.domain.region;

import java.util.Optional;

public interface RegionRepository {
    Optional<Region> findByUserId(String userId);

    void save(Region region);
}
