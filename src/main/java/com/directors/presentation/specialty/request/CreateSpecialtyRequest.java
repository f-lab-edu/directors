package com.directors.presentation.specialty.request;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyInfo;
import com.directors.domain.specialty.SpecialtyProperty;
import lombok.Builder;

@Builder
public record CreateSpecialtyRequest(
        String specialtyProperty,
        String description
) {
    public Specialty toEntity() {
        return Specialty.builder()
                .specialtyInfo(new SpecialtyInfo(SpecialtyProperty.fromValue(specialtyProperty), description))
                .build();
    }
}
