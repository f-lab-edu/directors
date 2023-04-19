package com.directors.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRegionRepository {
    Optional<UserRegion> findByUserId(String userId);

    List<UserRegion> findByFullAddress(String fullAddress);

    boolean existsByUserId(String userId);

    UserRegion save(UserRegion userRegion);
}
