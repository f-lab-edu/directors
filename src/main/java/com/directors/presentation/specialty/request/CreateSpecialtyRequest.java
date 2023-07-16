package com.directors.presentation.specialty.request;

import com.directors.domain.specialty.Specialty;
import com.directors.domain.specialty.SpecialtyProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
public record CreateSpecialtyRequest(
        String specialtyProperty,
        String description
) {
}
