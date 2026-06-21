package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GrupoResponse(
    UUID id,
    String name,
    String accessCode,
    UUID ownerId,
    UUID championshipId,
    List<UUID> memberIds,
    Instant createdAt
) {
}

