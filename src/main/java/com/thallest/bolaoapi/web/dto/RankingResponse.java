package com.thallest.bolaoapi.web.dto;

import java.util.List;

public record RankingResponse(
    Long groupId,
    String groupName,
    Long championshipId,
    List<RankingEntryResponse> standings
) {
}

