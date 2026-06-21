package com.thallest.bolaoapi.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PalpiteRequest(
    @NotNull UUID groupId,
    @NotNull UUID matchId,
    @NotNull @Min(0) Integer homeScoreGuess,
    @NotNull @Min(0) Integer awayScoreGuess
) {
}

