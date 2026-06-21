package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.time.LocalDate;

public record CampeonatoResponse(
    Long id,
    String name,
    String season,
    String description,
    LocalDate startDate,
    LocalDate endDate,
    boolean active,
    Instant createdAt
) {
}

