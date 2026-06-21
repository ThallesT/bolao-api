package com.thallest.bolaoapi.web.dto;

import com.thallest.bolaoapi.domain.MatchStatus;
import java.time.Instant;
import java.time.LocalDateTime;

public record PartidaResponse(
    Long id,
    Long championshipId,
    String homeTeam,
    String awayTeam,
    LocalDateTime matchDate,
    String roundName,
    String stage,
    MatchStatus status,
    Integer homeScore,
    Integer awayScore,
    Instant createdAt
) {
}

