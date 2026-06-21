package com.thallest.bolaoapi.web;

import com.thallest.bolaoapi.service.auth.AuthService;
import com.thallest.bolaoapi.web.dto.AuthResponse;
import com.thallest.bolaoapi.web.dto.GoogleAuthorizationResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/google/login")
    public GoogleAuthorizationResponse googleLogin(@RequestParam(required = false) String state) {
        return authService.getGoogleAuthorizationUrl(state);
    }

    @GetMapping("/google/callback")
    public AuthResponse googleCallback(@RequestParam String code) {
        return authService.loginWithGoogleCallback(code);
    }
}

