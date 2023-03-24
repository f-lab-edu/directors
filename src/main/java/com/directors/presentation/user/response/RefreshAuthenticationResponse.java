package com.directors.presentation.user.response;

public record RefreshAuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
