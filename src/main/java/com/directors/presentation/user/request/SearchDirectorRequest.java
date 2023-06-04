package com.directors.presentation.user.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;


@Builder
public record SearchDirectorRequest(
        @Min(value = 1, message = "지역의 범위는 1보다 크거나 같아야 합니다.")
        @Max(value = 5, message = "지역의 범위는 5보다 작거나 같아야 합니다.")
        Integer distance,
        @Nullable
        String property,
        @NotNull(message = "일정이 있는지 여부를 지정해야 합니다.")
        Boolean hasSchedule,
        @Nullable
        String searchText,
        @NotNull(message = "페이지를 지정해야 합니다.")
        @Min(value = 1, message = "페이지는 1보다 크거나 같아야 합니다.")
        Integer page,
        @NotNull(message = "사이즈를 지정해야 합니다.")
        @Min(value = 1, message = "사이즈는 1보다 크거나 같아야 합니다.")
        Integer size
) {
}
