package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InmemeryUserRegionRepository implements UserRegionRepository {
    Map<String, UserRegion> userRegionMap = new HashMap<>();

    private long nextId = 1;

    @Override
    public Optional<UserRegion> findByUserId(String userId) {
        return userRegionMap.values()
                .stream()
                .filter(ur -> ur.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userRegionMap.values()
                .stream()
                .anyMatch(ur -> ur.getUserId().equals(userId));
    }

    @Override
    public UserRegion save(UserRegion userRegion) {
        if (userRegion.getId() == null) {
            userRegion.setId(String.valueOf(nextId++));
        }
        userRegionMap.put(userRegion.getId(), userRegion);

        return userRegion;
    }
}