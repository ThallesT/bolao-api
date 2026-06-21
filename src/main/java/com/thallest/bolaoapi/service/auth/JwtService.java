package com.thallest.bolaoapi.service.auth;

import com.thallest.bolaoapi.config.AuthProperties;
import com.thallest.bolaoapi.domain.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final AuthProperties authProperties;
    private final SecretKey secretKey;

    public JwtService(AuthProperties authProperties) {
        this.authProperties = authProperties;
        this.secretKey = Keys.hmacShaKeyFor(authProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserEntity user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(authProperties.getJwt().getExpirationSeconds());

        return Jwts.builder()
            .subject(user.getId().toString())
            .issuer(authProperties.getJwt().getIssuer())
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claim("email", user.getEmail())
            .claim("name", user.getName())
            .claim("picture", user.getPhotoUrl())
            .signWith(secretKey)
            .compact();
    }

    public UUID extractUserId(String token) {
        return UUID.fromString(parseClaims(token).getSubject());
    }

    public boolean isValid(String token) {
        parseClaims(token);
        return true;
    }

    public long getExpirationSeconds() {
        return authProperties.getJwt().getExpirationSeconds();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}

