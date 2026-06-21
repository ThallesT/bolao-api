package com.thallest.bolaoapi.service.google;

import com.thallest.bolaoapi.config.AuthProperties;
import com.thallest.bolaoapi.web.exception.BusinessException;
import com.thallest.bolaoapi.web.exception.UnauthorizedException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

@Service
public class GoogleOAuthService {

    private final RestClient restClient;
    private final AuthProperties authProperties;

    public GoogleOAuthService(RestClient restClient, AuthProperties authProperties) {
        this.restClient = restClient;
        this.authProperties = authProperties;
    }

    public String buildAuthorizationUrl(String state) {
        validateGoogleConfig();

        Map<String, String> params = new LinkedHashMap<>();
        params.put("client_id", authProperties.getGoogle().getClientId());
        params.put("redirect_uri", authProperties.getGoogle().getRedirectUri());
        params.put("response_type", "code");
        params.put("scope", authProperties.getGoogle().getScope());
        params.put("access_type", "offline");
        params.put("prompt", "consent");
        if (StringUtils.hasText(state)) {
            params.put("state", state);
        }

        String query = params.entrySet().stream()
            .map(entry -> encode(entry.getKey()) + "=" + encode(entry.getValue()))
            .reduce((left, right) -> left + "&" + right)
            .orElse("");

        return authProperties.getGoogle().getAuthorizationUri() + "?" + query;
    }

    public GoogleUserInfo fetchUserInfoFromCode(String code) {
        validateGoogleConfig();

        GoogleTokenResponse tokenResponse = restClient.post()
            .uri(authProperties.getGoogle().getTokenUri())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body("code=" + encode(code)
                + "&client_id=" + encode(authProperties.getGoogle().getClientId())
                + "&client_secret=" + encode(authProperties.getGoogle().getClientSecret())
                + "&redirect_uri=" + encode(authProperties.getGoogle().getRedirectUri())
                + "&grant_type=authorization_code")
            .retrieve()
            .body(GoogleTokenResponse.class);

        if (tokenResponse == null || !StringUtils.hasText(tokenResponse.accessToken())) {
            throw new UnauthorizedException("Google token exchange failed.");
        }

        GoogleUserInfo userInfo = restClient.get()
            .uri(authProperties.getGoogle().getUserInfoUri())
            .headers(headers -> headers.setBearerAuth(tokenResponse.accessToken()))
            .retrieve()
            .body(GoogleUserInfo.class);

        if (userInfo == null || !StringUtils.hasText(userInfo.sub()) || !StringUtils.hasText(userInfo.email())) {
            throw new UnauthorizedException("Google user info response is incomplete.");
        }
        if (!Boolean.TRUE.equals(userInfo.emailVerified())) {
            throw new UnauthorizedException("Google account email must be verified.");
        }

        return userInfo;
    }

    private void validateGoogleConfig() {
        if (!StringUtils.hasText(authProperties.getGoogle().getClientId())
            || !StringUtils.hasText(authProperties.getGoogle().getClientSecret())) {
            throw new BusinessException("Google OAuth is not configured.");
        }
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

