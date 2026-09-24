package com.pedromolon.auth_service.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String token,
        Long expiresIn
) {
}
