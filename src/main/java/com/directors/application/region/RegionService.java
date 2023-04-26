package com.directors.application.region;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;
import com.directors.domain.user.exception.UserRegionNotFoundException;
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

    private final RegionRepository regionRepository;
    private final UserRegionRepository userRegionRepository;

    @PostConstruct
    @Transactional
    void loadRegionData() {
        // TODO: 04.22 추후 분산 서버 환경을 고려한 로직 변경이 필요.
        if (regionRepository.count() != 0) {
            return;
        }

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
        UserRegion userRegion = getUserRegion(userId);

        return getNearestRegion(userRegion.getRegion(), distance)
                .stream()
                .map(Region::getAddress)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Long> getNearestRegionId(String userId, int distance) {
        UserRegion userRegion = getUserRegion(userId);

        return getNearestRegion(userRegion.getRegion(), distance)
                .stream()
                .map(Region::getId)
                .collect(Collectors.toList());
    }

    private UserRegion getUserRegion(String userId) {
        var userRegion = userRegionRepository
                .findByUserId(userId)
                .orElseThrow(() -> new UserRegionNotFoundException(userId));
        return userRegion;
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
        return regionRepository.findRegionWithin(region.getPoint(), distance * KILOMETER);
    }
}
