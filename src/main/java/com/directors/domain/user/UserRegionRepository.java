package com.directors.domain.user;

import java.util.Optional;

public interface UserRegionRepository {
    Optional<UserRegion> findByUserId(String userId);

    boolean existsByUserId(String userId);

    UserRegion save(UserRegion userRegion);
}
