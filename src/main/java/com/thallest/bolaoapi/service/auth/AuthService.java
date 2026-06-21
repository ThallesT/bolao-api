package com.thallest.bolaoapi.service.auth;

import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.service.UserService;
import com.thallest.bolaoapi.service.google.GoogleOAuthService;
import com.thallest.bolaoapi.service.google.GoogleUserInfo;
import com.thallest.bolaoapi.web.dto.AuthResponse;
import com.thallest.bolaoapi.web.dto.GoogleAuthorizationResponse;
import com.thallest.bolaoapi.web.dto.UserResponse;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final GoogleOAuthService googleOAuthService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthService(
        GoogleOAuthService googleOAuthService,
        UserService userService,
        JwtService jwtService
    ) {
        this.googleOAuthService = googleOAuthService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Transactional(readOnly = true)
    public GoogleAuthorizationResponse getGoogleAuthorizationUrl(String state) {
        return new GoogleAuthorizationResponse(googleOAuthService.buildAuthorizationUrl(state));
    }

    public AuthResponse loginWithGoogleCallback(String code) {
        GoogleUserInfo googleUser = googleOAuthService.fetchUserInfoFromCode(code);
        UserEntity user = userService.resolveOrCreateFromGoogle(googleUser.sub(), googleUser.email(), googleUser.name(), googleUser.picture());
        String token = jwtService.generateToken(user);
        UserResponse userResponse = userService.findById(user.getId());

        return new AuthResponse(
            token,
            "Bearer",
            jwtService.getExpirationSeconds(),
            userResponse
        );
    }

    public UserEntity authenticate(String token) {
        UUID userId = jwtService.extractUserId(token);
        return userService.getEntity(userId);
    }
}

