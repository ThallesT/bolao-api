package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.util.List;

public record GrupoResponse(
    Long id,
    String name,
    String accessCode,
    Long ownerId,
    Long championshipId,
    List<Long> memberIds,
    Instant createdAt
) {
}

