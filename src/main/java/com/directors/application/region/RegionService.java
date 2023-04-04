package com.directors.application.region;

import com.directors.domain.region.Region;
import com.directors.domain.region.RegionRepository;
import com.directors.domain.user.UserRegion;
import com.directors.domain.user.UserRegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionService {

    private final RegionRepository regionRepository;
    private final UserRegionRepository userRegionRepository;

    @Transactional
    public List<String> getNearestAddress(String userId, int distance) {
        UserRegion userRegion = userRegionRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException()); // TODO: 04.05 먼저 지역 인증이 필요하다는 예외가 필요.
        Region region = regionRepository.findByRegionId(userRegion.getRegionId());

        return getNearestRegion(region, distance)
                .stream()
                .map(reg -> reg.getAddress().getUnitAddress())
                .collect(Collectors.toList());
    }

    private List<Region> getNearestRegion(Region region, int distance) {
        List<Region> regionWithin = regionRepository.findRegionWithin(region, distance * 1000);
        return regionWithin;
    }

    @PostConstruct
    private void loadRegionData() {
        String pathPrefix = "/regionCSV/";
        String pathSuffix = "_좌표.csv";
        String[] regions = {
                "강원", "경기", "경남", "경북", "광주", "대구", "대전", "부산", "서울", "세종", "울산", "인천", "전남", "전북", "제주", "충남", "충북"
        };

        for (int i = 0; i < regions.length; i++) {
            List<String> regionDataLines = null;

            InputStream inputStream = getClass().getResourceAsStream(pathPrefix + regions[i] + pathSuffix);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            regionDataLines = reader.lines().collect(Collectors.toList());

            List<Region> collect = regionDataLines.stream()
                    .map(this::regionDataLineToRegion)
                    .collect(Collectors.toList());

            regionRepository.saveAll(collect);
        }
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
}
