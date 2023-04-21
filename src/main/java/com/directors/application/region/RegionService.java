package com.directors.application.region;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.infrastructure.exception.user.UserRegionNotFoundException;
import com.directors.infrastructure.jpa.region.JpaRegionRepository;
import com.directors.infrastructure.jpa.user.JpaUserRegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {
    private static final int KILOMETER = 1000;

    private final JpaRegionRepository regionRepository;
    private final JpaUserRegionRepository userRegionRepository;

    @PostConstruct
    @Transactional
    void loadRegionData() {
        String pathPrefix = "/regionCSV/";
        String pathSuffix = "_좌표.csv";
        String[] regions = {
                "강원", "경기", "경남", "경북", "광주", "대구", "대전", "부산", "서울", "세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북"
        };

        for (int i = 0; i < regions.length; i++) {
            List<String> regionDataLines = null;

            var inputStream = getClass().getResourceAsStream(pathPrefix + regions[i] + pathSuffix);
            var reader = new BufferedReader(new InputStreamReader(inputStream));
            regionDataLines = reader.lines().collect(Collectors.toList());

            List<Region> collect = regionDataLines.stream()
                    .map(this::regionDataLineToRegion).toList();

            regionRepository.saveAll(collect);
        }
    }

    @Transactional
    public List<Address> getNearestAddress(String userId, int distance) {
        var userRegion = userRegionRepository
                .findByUserId(userId)
                .orElseThrow(() -> new UserRegionNotFoundException(userId));
        var region = regionRepository.findById(userRegion.getRegion().getId()).orElseThrow();

        return getNearestRegion(region, distance)
                .stream()
                .map(reg -> reg.getAddress())
                .collect(Collectors.toList());
    }

    private Region regionDataLineToRegion(String regionLine) {
        String[] lineSplit = regionLine.split(",");
        return Region.of(lineSplit[0], lineSplit[1], coordinateToPoint(lineSplit[2], lineSplit[3]));
    }

    private Point coordinateToPoint(String longitude, String latitude) {
        double lon = Double.parseDouble(longitude);
        double lat = Double.parseDouble(latitude);
        return new GeometryFactory().createPoint(new Coordinate(lon, lat));
    }

    private List<Region> getNearestRegion(Region region, int distance) {
        return regionRepository.findRegionByPointDistanceLessThan(region.getPoint(), distance * KILOMETER);
    }
}
