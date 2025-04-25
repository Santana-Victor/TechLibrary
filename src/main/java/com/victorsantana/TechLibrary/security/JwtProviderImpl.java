package com.victorsantana.TechLibrary.security;

import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class JwtProviderImpl implements JwtProvider {

    private final JwtEncoder jwtEncoder;

    public JwtProviderImpl(JwtEncoder jwtEncoder) { this.jwtEncoder = jwtEncoder; }

    @Override
    public String generateToken(Instant createdAt, Instant expiresAt, UUID id) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(expiresAt)
                .issuer("tech_library")
                .issuedAt(createdAt)
                .subject(id.toString())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}