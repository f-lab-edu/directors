package com.directors.infrastructure.jpa.user;

import com.directors.domain.user.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaUserRegionRepository extends JpaRepository<UserRegion, Long> {
    Optional<UserRegion> findByUserId(String userId);

    List<UserRegion> findByAddressFullAddress(String fullAddress);

    boolean existsByUserId(String userId);
}
