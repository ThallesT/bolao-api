package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    String photoUrl,
    String googleSubject,
    Instant createdAt
) {
}

