package com.directors.domain.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.locationtech.jts.geom.Point;

@AllArgsConstructor
@Getter
@Builder
public class Region {
    private String id;

    private Address address;

    private Point point;

    public void setId(String id) {
        this.id = id;
    }

    public static Region of(String fullAddress, String unitAddress, Point point) {
        return Region.builder()
                .address(new Address(fullAddress, unitAddress))
                .point(point)
                .build();
    }
}
