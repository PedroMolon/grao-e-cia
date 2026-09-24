package com.pedromolon.auth_service.dto.response;

import lombok.Builder;

@Builder
public record RegisterResponse(
        Long id,
        String user
) {
}
