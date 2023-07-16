package com.directors.domain.region;

import com.directors.IntegrationTestSupport;
import com.directors.domain.region.exception.RegionNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @DisplayName("존재하지 않는 full address로 지역을 조회하면 예외가 발생한다.")
    @Test
    void findByWrongFullAddress() {
        // given
        String givenWrongFullAddress = "서울특별시 성동구";

        // when then
        assertThatThrownBy(() -> regionRepository
                .findByFullAddress(givenWrongFullAddress)
                .orElseThrow(() -> new RegionNotFoundException(givenWrongFullAddress))
        )
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("존재하지 않는 지역입니다.");
    }

    /**
     * h2 환경에서는 MYSQL의 ST_Distance 메서드가 작동하지 않으므로 disable 처리했습니다.
     * 이 메서드를 테스트할 경우, test 시의 datasource를 MYSQL로 전환해야 합니다.
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
                .findRegionWithin(region.getPoint().getX(), region.getPoint().getY(), givenDistance);

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

    @DisplayName("지역 아이디를 통해 지역을 조회한다.")
    @Test
    void findRegionById() {
        // given
        Long givenRegionId = 10L;

        // when
        Region region = regionRepository.findById(givenRegionId)
                .orElseThrow(() -> new RegionNotFoundException(null));

        // then
        assertThat(region.getId()).isEqualTo(givenRegionId);
        assertThat(region.getAddress())
                .extracting("fullAddress", "unitAddress")
                .contains("강원도 양양군 강현면 답리", "답리");
    }

    @DisplayName("잘못된 지역 아이디를 통해 지역을 조회하면 예외가 발생한다.")
    @Test
    void findRegionByWrongId() {
        // given
        Long givenRegionId = -1L;

        // when then
        assertThatThrownBy(() -> regionRepository.findById(givenRegionId)
                .orElseThrow(() -> new RegionNotFoundException(null)))
                .isInstanceOf(RegionNotFoundException.class)
                .hasMessage("존재하지 않는 지역입니다.");
    }

    @DisplayName("데이터베이스에 저장된 전체 지역의 수를 조회한다.")
    @Test
    void count() {
        // given
        Long allRegionCount = 21541L;

        // when
        Long count = regionRepository.count();

        // then
        assertThat(count).isEqualTo(allRegionCount);
    }

}