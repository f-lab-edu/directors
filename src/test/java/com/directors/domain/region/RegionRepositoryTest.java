package com.directors.domain.region;

import com.directors.IntegrationTestSupport;
import com.directors.domain.region.exception.RegionNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}