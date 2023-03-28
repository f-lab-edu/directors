package com.directors.domain.user;

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

    private String region;

    private long reward;

    private UserStatus status;

    private Date joinedDate;

    private Date withdrawalDate;

    private List<Field> fields;

    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void withdrawal(Date withdrawalDate) {
        this.status = UserStatus.WITHDRAWN;
        this.withdrawalDate = withdrawalDate;
    }
}
