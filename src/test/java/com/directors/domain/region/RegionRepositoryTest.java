package com.directors.domain.region;

import com.directors.IntegrationTestSupport;
import com.directors.domain.region.exception.RegionNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RegionRepositoryTest extends IntegrationTestSupport {

    @Autowired
    RegionRepository regionRepository;

    @DisplayName("full address로 지역을 조회한다.")
    @Test
    void findByFullAddress() {
        // given
        String givenFullAddress = "서울특별시 성동구 송정동";

        // when
        Region region = regionRepository
                .findByFullAddress(givenFullAddress)
                .orElseThrow(() -> new RegionNotFoundException(givenFullAddress));

        // then
        assertThat(region.getAddress().getFullAddress()).isEqualTo(givenFullAddress);
    }

    /**
     * h2 환경에서는 MYSQL의 ST_Distance 메서드가 작동하지 않으므로 disable 처리함.
     */
    @Disabled
    @DisplayName("주어진 위치과 일정 거리 안에 위치한 지역을 조회한다.")
    @Test
    void findRegionWithin() {
        // given
        String givenFullAddress = "서울특별시 성동구 송정동";
        double givenDistance = 1000;

        Region region = regionRepository
                .findByFullAddress(givenFullAddress)
                .orElseThrow(null);

        // when
        List<Region> regionWithin = regionRepository
                .findRegionWithin(region.getPoint(), givenDistance);

        // then
        assertThat(regionWithin).hasSize(3)
                .extracting(address -> address.getAddress().getFullAddress())
                .containsExactlyInAnyOrder(
                        "서울특별시 광진구 군자동",
                        "서울특별시 광진구 화양동",
                        "서울특별시 성동구 송정동"
                );
        assertThat(regionWithin).extracting(address -> address.getAddress().getUnitAddress())
                .containsExactlyInAnyOrder(
                        "군자동",
                        "화양동",
                        "송정동"
                );
    }
}