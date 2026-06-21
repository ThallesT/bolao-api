package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.domain.UserEntity;
import com.thallest.bolaoapi.service.auth.AuthService;
import com.thallest.bolaoapi.service.UserService;
import com.thallest.bolaoapi.web.dto.AuthResponse;
import com.thallest.bolaoapi.web.dto.GoogleAuthorizationResponse;
import com.thallest.bolaoapi.web.dto.UserResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @GetMapping("/google/login")
    public GoogleAuthorizationResponse googleLogin(@RequestParam(required = false) String state) {
        return authService.getGoogleAuthorizationUrl(state);
    }

    @GetMapping("/google/callback")
    public AuthResponse googleCallback(@RequestParam String code) {
        return authService.loginWithGoogleCallback(code);
    }

    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal UserEntity currentUser) {
        return userService.findById(currentUser.getId());
    }
}

