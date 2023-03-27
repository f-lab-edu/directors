package com.directors.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class User {
    private String userId;

    private String password;

    private String name;

    private String nickname;

    private String email;

    private String phoneNumber;

    private String region;

    private long reward;

    private UserStatus status;

    private Date joinedDate;

    private Date withdrawalDate;
    
    public void setPasswordByEncryption(String encryptedPassword) {
        this.password = encryptedPassword;
    }

    public void setStatus(UserStatus userStatus) {
        this.status = userStatus;
    }

    public void setWithdrawalDate(Date withdrawalDate) {
        this.withdrawalDate = withdrawalDate;
    }
}
