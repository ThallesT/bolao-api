package com.thallest.bolaoapi.web.dto;

import java.time.Instant;

public record PalpiteResponse(
    Long id,
    Long groupId,
    Long matchId,
    Long userId,
    Integer homeScoreGuess,
    Integer awayScoreGuess,
    Instant createdAt,
    Instant updatedAt
) {
}

