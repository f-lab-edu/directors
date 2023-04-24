package com.directors.infrastructure.jpa.region;

import com.directors.domain.region.Region;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaRegionRepository extends JpaRepository<Region, Long> {
    Optional<Region> findByAddressFullAddress(String fullAddress);

    @Query("SELECT r FROM Region r WHERE ST_Distance(r.point, :point) <= :distance")
    List<Region> findRegionByPointDistanceLessThan(@Param("point") Point point, @Param("distance") double distance);
}
