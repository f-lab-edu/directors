package com.directors.presentation.user.request;

import com.directors.domain.specialty.SpecialtyProperty;
import jakarta.validation.constraints.Size;

public record SearchDirectorRequest(
        @Size(min = 1, max = 5, message = "지역의 범위가 1-5 사이로 입력되지 않았습니다.")
        int distance,
        SpecialtyProperty specialtyProperty,
        boolean hasSchedule,
        String searchText,
        int page,
        int size
) {
}
