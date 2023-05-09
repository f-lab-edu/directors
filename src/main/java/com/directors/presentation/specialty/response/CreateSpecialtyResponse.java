package com.directors.presentation.specialty.response;

import com.directors.domain.specialty.Specialty;
import lombok.Builder;

@Builder
public record CreateSpecialtyResponse(
        Long id,
        String specialtyProperty,
        String description
) {
    public static CreateSpecialtyResponse from(Specialty specialty) {
        return CreateSpecialtyResponse.builder()
                .id(specialty.getId())
                .specialtyProperty(specialty.getSpecialtyInfo().getProperty().getValue())
                .description(specialty.getSpecialtyInfo().getDescription())
                .build();
    }
}
