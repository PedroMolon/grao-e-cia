package com.pedromolon.auth_service.service;

import com.pedromolon.auth_service.domain.Role;
import com.pedromolon.auth_service.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    public TokenService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(User user) {
        Instant instant = Instant.now();
        long expiresIn = 3600L;

        List<String> roles = user.getRoles().stream()
                .map(Role::getName)
                .toList();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("minha-api")
                .subject(user.getName())
                .claim("userId", user.getId().toString())
                .claim("email", user.getEmail())
                .claim("roles", roles)
                .issuedAt(instant)
                .expiresAt(instant.plusSeconds(expiresIn))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
