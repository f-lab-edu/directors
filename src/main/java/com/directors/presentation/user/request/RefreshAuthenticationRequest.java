package com.directors.presentation.user.request;

public record RefreshAuthenticationRequest(
        String accessToken,
        String refreshToken
) {
}
