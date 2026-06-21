package com.thallest.bolaoapi.web.dto;

public record RankingEntryResponse(
    Long userId,
    String userName,
    int points,
    int exactHits,
    int outcomeHits,
    int scoredGuesses,
    int totalGuesses
) {
}

