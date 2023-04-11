package com.directors.domain.region;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record Address (
        String fullAddress,
        String unitAddress
){
}
