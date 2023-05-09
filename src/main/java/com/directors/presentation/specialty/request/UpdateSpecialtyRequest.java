package com.directors.presentation.specialty.request;

import lombok.Builder;

@Builder
public record UpdateSpecialtyRequest(
        Long id,
        String property,
        String description
) {
}
