package com.thallest.bolaoapi.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CampeonatoRequest(
    @NotBlank @Size(max = 140) String name,
    @NotBlank @Size(max = 60) String season,
    @Size(max = 500) String description,
    LocalDate startDate,
    LocalDate endDate,
    @NotNull Boolean active
) {
}

