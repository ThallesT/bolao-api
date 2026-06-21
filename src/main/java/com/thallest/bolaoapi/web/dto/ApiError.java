package com.thallest.bolaoapi.web.dto;

import java.time.Instant;
import java.util.List;

public record ApiError(
    Instant timestamp,
    int status,
    String error,
    List<String> details
) {
}

