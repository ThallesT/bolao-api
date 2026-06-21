package com.thallest.bolaoapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public class AuthProperties {

    private final Jwt jwt = new Jwt();
    private final Google google = new Google();

    public Jwt getJwt() {
        return jwt;
    }

    public Google getGoogle() {
        return google;
    }

    public static class Jwt {
        private String issuer = "bolao-api";
        private String secret = "change-me-change-me-change-me-change-me";
        private long expirationSeconds = 86400;

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getExpirationSeconds() {
            return expirationSeconds;
        }

        public void setExpirationSeconds(long expirationSeconds) {
            this.expirationSeconds = expirationSeconds;
        }
    }

    public static class Google {
        private String clientId = "";
        private String clientSecret = "";
        private String redirectUri = "http://localhost:8080/api/auth/google/callback";
        private String scope = "openid email profile";
        private String authorizationUri = "https://accounts.google.com/o/oauth2/v2/auth";
        private String tokenUri = "https://oauth2.googleapis.com/token";
        private String userInfoUri = "https://openidconnect.googleapis.com/v1/userinfo";

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public void setClientSecret(String clientSecret) {
            this.clientSecret = clientSecret;
        }

        public String getRedirectUri() {
            return redirectUri;
        }

        public void setRedirectUri(String redirectUri) {
            this.redirectUri = redirectUri;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getAuthorizationUri() {
            return authorizationUri;
        }

        public void setAuthorizationUri(String authorizationUri) {
            this.authorizationUri = authorizationUri;
        }

        public String getTokenUri() {
            return tokenUri;
        }

        public void setTokenUri(String tokenUri) {
            this.tokenUri = tokenUri;
        }

        public String getUserInfoUri() {
            return userInfoUri;
        }

        public void setUserInfoUri(String userInfoUri) {
            this.userInfoUri = userInfoUri;
        }
    }
}

