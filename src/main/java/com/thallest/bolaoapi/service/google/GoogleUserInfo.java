package com.thallest.bolaoapi.service.google;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GoogleUserInfo(
    @JsonProperty("sub") String sub,
    @JsonProperty("name") String name,
    @JsonProperty("email") String email,
    @JsonProperty("picture") String picture,
    @JsonProperty("email_verified") Boolean emailVerified
) {
}

