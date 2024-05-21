package com.aptner.v3.auth.dto;

import lombok.Builder;

@Builder
public record TokenDto(
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {

}

