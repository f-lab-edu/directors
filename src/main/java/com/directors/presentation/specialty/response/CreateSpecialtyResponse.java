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
                .specialtyProperty(specialty.getProperty().getValue())
                .description(specialty.getDescription())
                .build();
    }
}
