package com.directors.domain.region;

import com.directors.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "region")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Region extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @Column(name = "point", columnDefinition = "GEOMETRY")
    private Point point;

    public void setId(Long id) {
        this.id = id;
    }

    public static Region of(String fullAddress, String unitAddress, Point point) {
        return Region.builder()
                .address(new Address(fullAddress, unitAddress))
                .point(point)
                .build();
    }
}
