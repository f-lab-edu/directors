package com.directors.presentation.user.response;

public record LogInResponse(
        String accessToken,
        String refreshToken
) {
}
