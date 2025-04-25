package com.victorsantana.TechLibrary.security;

import java.time.Instant;
import java.util.UUID;

public interface JwtProvider {
    String generateToken(Instant createdAt, Instant expiresAt, UUID id);
}