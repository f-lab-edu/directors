package com.directors.presentation.user.request;

public record AuthenticateRegionRequest(
        double latitude,
        double longitude
) {
}
