package com.directors.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class Token {
    private String tokenString;
    private String userId;
    private Date expiration;
}
