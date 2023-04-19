package com.directors.presentation.user.response;

import com.directors.domain.specialty.SpecialtyInfo;

import java.util.List;

public record SearchDirectorResponse(
        String directorId,
        String directorName,
        List<SpecialtyInfo> specialtyInfos
) {
}
