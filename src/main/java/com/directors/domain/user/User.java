package com.directors.domain.user;

import com.directors.domain.specialty.Specialty;
import com.directors.infrastructure.exception.user.AuthenticationFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class User {
    private final String userId;

    private String password;

    private final String name;

    private final String nickname;

    private String email;

    private final String phoneNumber;

    private Long reward;

    private UserStatus status;

    private Date joinedDate;

    private Date withdrawalDate;

    private List<Specialty> specialtyList;

    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void changeEmail(String oldEmail, String newEmail) {
        validateEmail(oldEmail);
        this.email = newEmail;
    }

    public void setFields(List<Specialty> specialtyList) {
        this.specialtyList = specialtyList;
    }

    public void withdrawal(Date withdrawalDate) {
        this.status = UserStatus.WITHDRAWN;
        this.withdrawalDate = withdrawalDate;
    }

    private void validateEmail(String email) {
        if (this.email.equals(email)) {
            throw new AuthenticationFailedException(this.userId);
        }
    }
}
