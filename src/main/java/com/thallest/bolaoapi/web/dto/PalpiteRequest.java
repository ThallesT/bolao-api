package com.thallest.bolaoapi.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PalpiteRequest(
    @NotNull Long groupId,
    @NotNull Long matchId,
    @NotNull Long userId,
    @NotNull @Min(0) Integer homeScoreGuess,
    @NotNull @Min(0) Integer awayScoreGuess
) {
}

