package com.directors.presentation.user.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record AuthenticateRegionRequest(
        @NotNull
        @Positive
        double latitude,
        @NotNull
        @Positive
        double longitude
) {
}
