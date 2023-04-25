package com.directors.domain.user;

import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "userregion")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class UserRegion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Address address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", referencedColumnName = "id")
    private Region region;

    public void updateRegionInfo(Address address, Region region) {
        this.address = address;
        this.region = region;
    }

    public static UserRegion of(Address address, User user, Region region) {
        return UserRegion.builder()
                .address(address)
                .user(user)
                .region(region)
                .build();
    }
}
