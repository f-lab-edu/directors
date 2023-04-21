package com.directors.presentation.specialty.request;

public record UpdateSpecialtyRequest(
        Long id,
        String property,
        String description,
        String userId
) {
}
