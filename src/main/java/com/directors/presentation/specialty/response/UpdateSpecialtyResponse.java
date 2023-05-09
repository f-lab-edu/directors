package com.directors.presentation.specialty.response;

import com.directors.domain.specialty.Specialty;
import lombok.Builder;

@Builder
public record UpdateSpecialtyResponse(
        Long id,
        String property,
        String description
) {
    public static UpdateSpecialtyResponse from(Specialty specialty) {
        return UpdateSpecialtyResponse.builder()
                .id(specialty.getId())
                .property(specialty.getSpecialtyInfo().getProperty().getValue())
                .description(specialty.getSpecialtyInfo().getDescription())
                .build();
    }
}
