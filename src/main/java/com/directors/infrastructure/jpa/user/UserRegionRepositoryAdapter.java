package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRegionRepositoryAdapter implements UserRegionRepository {
    private final JpaUserRegionRepository userRegionRepository;

    @Override
    public Optional<UserRegion> findByUserId(String userId) {
        return userRegionRepository.findByUserId(userId);
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userRegionRepository.existsByUserId(userId);
    }

    @Override
    public UserRegion save(UserRegion userRegion) {
        return userRegionRepository.save(userRegion);
    }
}
