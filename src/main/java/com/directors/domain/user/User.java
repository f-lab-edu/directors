package com.directors.domain.user;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.user.exception.NotEnoughRewardException;
import com.directors.domain.user.exception.UserRegionNotFoundException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User extends BaseEntity {
    @Id
    private String id;

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Long reward;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private LocalDateTime withdrawalDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Specialty> specialtyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_Id")
    private Region region;

    public Address getUserAddress() {
        if (region == null) {
            return null;
        }
        return region.getAddress();
    }

    public Region getRegion() {
        if (region == null) {
            return null;
            // TODO: 06.28 로그인하지 않은 접속자를 고려하여 지역 인증 후 디렉터 리스트를 조회한다는 기존 로직을 무효화함. 추후 다시 고려 필요.
            // throw new UserRegionNotFoundException(this.id);
        }
        return region;
    }

    public List<SpecialtyInfo> getSpecialtyInfoList() {
        if (specialtyList == null) {
            return new ArrayList<>();
        }
        return specialtyList
                .stream()
                .map(Specialty::getSpecialtyInfo)
                .collect(Collectors.toList());
    }

    public List<LocalDateTime> getScheduleStartTimes() {
        if (scheduleList.isEmpty()) {
            return Collections.emptyList();
        }
        return scheduleList.stream()
                .map(Schedule::getStartTime)
                .collect(Collectors.toUnmodifiableList());
    }

    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeEmail(String newEmail) {
        this.email = newEmail;
    }

    public void withdrawal(LocalDateTime withdrawalDate) {
        this.userStatus = UserStatus.WITHDRAWN;
        this.withdrawalDate = withdrawalDate;
    }

    public void addReword() {
        this.reward += 1L;
    }

    public void paymentReward() {
        if (this.reward <= 0) {
            throw new NotEnoughRewardException(this.id);
        }
        this.reward -= 1L;
    }

    public void authenticateRegion(Region region) {
        this.region = region;
    }
}
