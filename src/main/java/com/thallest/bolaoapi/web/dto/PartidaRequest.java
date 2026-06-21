package com.thallest.bolaoapi.web.dto;

import com.thallest.bolaoapi.domain.MatchStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record PartidaRequest(
    @NotNull Long championshipId,
    @NotBlank @Size(max = 100) String homeTeam,
    @NotBlank @Size(max = 100) String awayTeam,
    @NotNull LocalDateTime matchDate,
    @Size(max = 80) String roundName,
    @Size(max = 80) String stage,
    @NotNull MatchStatus status,
    Integer homeScore,
    Integer awayScore
) {
}

