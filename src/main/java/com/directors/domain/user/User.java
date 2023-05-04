package com.directors.domain.user;

import com.directors.domain.common.BaseEntity;
import com.directors.domain.region.Address;
import com.directors.domain.schedule.Schedule;
import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.user.exception.AuthenticationFailedException;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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

    private Date withdrawalDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Specialty> specialtyList = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserRegion userRegion;

    public Address getUserAddress() {
        if (userRegion == null) {
            return null;
        }
        return userRegion.getAddress();
    }

    public List<SpecialtyInfo> getSpecialtyInfoList() {
        return specialtyList
                .stream()
                .map(Specialty::getSpecialtyInfo)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<LocalDateTime> getScheduleStartTimes() {
        return scheduleList.stream()
                .map(Schedule::getStartTime)
                .collect(Collectors.toList());
    }

    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeEmail(String oldEmail, String newEmail) {
        validateEmail(oldEmail);
        this.email = newEmail;
    }

    public void withdrawal(Date withdrawalDate) {
        this.userStatus = UserStatus.WITHDRAWN;
        this.withdrawalDate = withdrawalDate;
    }

    private void validateEmail(String email) {
        if (!this.email.equals(email)) {
            throw new AuthenticationFailedException(this.id);
        }
    }
}
