package com.directors.presentation.specialty.request;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;

public record UpdateSpecialtyRequest(
        String id,
        String property,
        String description,
        String userId
) {
    public Specialty toEntity() {
        return Specialty.builder()
                .id(id)
                .property(SpecialtyProperty.fromValue(property))
                .description(description)
                .userId(userId)
                .build();
    }
}
