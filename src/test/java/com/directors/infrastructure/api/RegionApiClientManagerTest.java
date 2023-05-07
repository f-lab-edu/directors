package com.directors.infrastructure.api;

import com.directors.IntegrationTestSupport;
import com.directors.domain.region.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RegionApiClientManagerTest extends IntegrationTestSupport {

    @Autowired
    private RegionApiClientManager regionApiClientManager;

    @DisplayName("UTM-K 기반 위치 정보를 통해 주소를 조회한다.")
    @Test
    void findRegionAddressByLocation() {
        // given
        long latitude = 961487;
        long longitude = 1949977;

        // when
        Address address = regionApiClientManager.findRegionAddressByLocation(latitude, longitude);

        // then
        assertThat(address)
                .extracting("fullAddress", "unitAddress")
                .contains("서울특별시 성동구 송정동", "송정동");
    }

    @DisplayName("잘못된 위치 정보를 통해 주소를 조회할 경우 예외가 발생한다.")
    @Test
    void findRegionAddressByWrongLocation() {
        // given
        long latitude = 10;
        long longitude = 10;

        // when then
        assertThatThrownBy(() -> regionApiClientManager.findRegionAddressByLocation(latitude, longitude))
                .isInstanceOf(IllegalArgumentException.class);
    }
}