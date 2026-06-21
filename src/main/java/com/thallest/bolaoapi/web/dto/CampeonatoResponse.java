package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record CampeonatoResponse(
    UUID id,
    String name,
    String season,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    Instant createdAt
) {
}

