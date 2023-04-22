package com.directors.presentation.specialty.request;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;

public record CreateSpecialtyRequest(
        SpecialtyProperty property,
        String description
) {
    public Specialty toEntity() {
        return Specialty.builder()
                .property(property)
                .description(description)
                .build();
    }
}
