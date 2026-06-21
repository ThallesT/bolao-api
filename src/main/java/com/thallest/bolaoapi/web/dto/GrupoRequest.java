package com.thallest.bolaoapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

public record GrupoRequest(
    @NotBlank @Size(max = 140) String name,
    @NotBlank @Size(max = 60) String accessCode,
    @NotNull Long ownerId,
    @NotNull Long championshipId,
    @NotEmpty List<Long> memberIds
) {
}

