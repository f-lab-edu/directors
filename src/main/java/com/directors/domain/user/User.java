package com.directors.domain.user;

import com.directors.domain.common.BaseEntity;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class User extends BaseEntity {
    @Id
    private String userId;

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String phoneNumber;

    private Long reward;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private Date withdrawalDate;

//    private List<Specialty> specialtyList;

    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeEmail(String oldEmail, String newEmail) {
        validateEmail(oldEmail);
        this.email = newEmail;
    }

//    public void setSpecialtyList(List<Specialty> specialtyList) {
//        this.specialtyList = specialtyList;
//    }

    public void withdrawal(Date withdrawalDate) {
        this.userStatus = UserStatus.WITHDRAWN;
        this.withdrawalDate = withdrawalDate;
    }

    private void validateEmail(String email) {
        if (!this.email.equals(email)) {
            throw new AuthenticationFailedException(this.userId);
        }
    }
}
