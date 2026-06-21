package com.thallest.bolaoapi.web.dto;

import com.thallest.bolaoapi.domain.MatchStatus;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record PartidaResponse(
    UUID id,
    UUID championshipId,
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

