package com.directors.domain.user;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.region.Address;
import com.directors.domain.region.Region;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.user.exception.NotEnoughRewardException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Column(nullable = true)
    private LocalDateTime withdrawalDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Specialty> specialtyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_Id")
    private Region region;

    @Builder
    public User(String id, String password, String name, String nickname, String email, String phoneNumber, UserStatus userStatus, Long reward) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.userStatus = userStatus;
        this.reward= reward;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public Address getUserAddress() {
        if (region == null) {
            return null;
        }
        return region.getAddress();
    }

    public Region getRegion() {
        // TODO: 06.28 로그인하지 않은 접속자를 고려하여 지역 인증 후 디렉터 리스트를 조회한다는 기존 로직을 무효화함. 추후 다시 고려 필요.
        // throw new UserRegionNotFoundException(this.id);
        return region;
    }

    public List<Specialty> getSpecialtyList() {
        if (specialtyList == null) {
            return Collections.emptyList();
        }
        return specialtyList;
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
