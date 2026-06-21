package com.thallest.bolaoapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

public record GrupoRequest(
    @NotBlank @Size(max = 140) String name,
    @NotBlank @Size(max = 60) String accessCode,
    @NotNull UUID ownerId,
    @NotNull UUID championshipId,
    @NotEmpty List<UUID> memberIds
) {
}

