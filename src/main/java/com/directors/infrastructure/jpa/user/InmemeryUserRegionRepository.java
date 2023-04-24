package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class InmemeryUserRegionRepository implements UserRegionRepository {
    Map<String, UserRegion> userRegionMap = new HashMap<>();

    private long nextId = 1;

    @Override
    public Optional<UserRegion> findByUserId(String userId) {
        return userRegionMap.values()
                .stream()
                .filter(ur -> ur.getUser().getId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserRegion> findByFullAddress(String fullAddress) {
        return userRegionMap.values()
                .stream()
                .filter(ur -> ur.getAddress().getFullAddress().equals(fullAddress))
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByUserId(String userId) {
        return userRegionMap.values()
                .stream()
                .anyMatch(ur -> ur.getUser().getId().equals(userId));
    }

    @Override
    public UserRegion save(UserRegion userRegion) {
//        if (userRegion.getId() == null) {
//            userRegion.setId(nextId++);
//        }
//        userRegionMap.put(userRegion.getId(), userRegion);

        return userRegion;
    }
}
