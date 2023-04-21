package com.directors.presentation.user.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SearchDirectorRequest(
        @Min(value = 0, message = "지역의 범위는 1보다 크거나 같아야 합니다.")
        @Max(value = 5, message = "지역의 범위는 1보다 작거나 같아야 합니다.")
        Integer distance,
        String property,
        @NotNull(message = "일정이 있는지 여부를 지정해야 합니다.")
        Boolean hasSchedule,
        String searchText,
        @NotNull(message = "페이지를 지정해야 합니다.")
        @Min(value = 1, message = "페이지는 1보다 크거나 같아야 합니다.")
        Integer page,
        @NotNull(message = "사이즈를 지정해야 합니다.")
        @Min(value = 1, message = "사이즈는 1보다 크거나 같아야 합니다.")
        Integer size
) {
}
