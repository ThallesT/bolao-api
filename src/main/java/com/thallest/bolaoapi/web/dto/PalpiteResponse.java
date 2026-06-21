package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.util.UUID;

public record PalpiteResponse(
    UUID id,
    UUID groupId,
    UUID matchId,
    UUID userId,
    Integer homeScoreGuess,
    Integer awayScoreGuess,
    Instant createdAt,
    Instant updatedAt
) {
}

