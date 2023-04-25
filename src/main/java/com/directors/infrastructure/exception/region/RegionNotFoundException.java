package com.directors.infrastructure.exception.region;

import lombok.Getter;

@Getter
public class RegionNotFoundException extends RuntimeException {
    private String fullAddress;
    private final static String message = "존재하지 않는 지역입니다.";

    public RegionNotFoundException(String fullAddress) {
        super(message);
        this.fullAddress = fullAddress;
    }
}
