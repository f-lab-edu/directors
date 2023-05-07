package com.directors.presentation.user.request;

import lombok.Builder;

@Builder
public record AuthenticateRegionRequest(
        double latitude,
        double longitude
) {
}
