package com.neobank.auth_service.controller;

import com.neobank.auth_service.dto.LoginRequest;
import com.neobank.auth_service.dto.LoginResponse;
import com.neobank.auth_service.dto.RefreshTokenRequest;
import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.dto.VerifyEmailRequest;
import com.neobank.auth_service.service.AuthenticationService;
import com.neobank.auth_service.service.OtpService;
import com.neobank.auth_service.service.RefreshTokenService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final OtpService otpService;
    private final RefreshTokenService refreshTokenService;

    public AuthController(
            AuthenticationService authenticationService,
            OtpService otpService,
            RefreshTokenService refreshTokenService) {

        this.authenticationService = authenticationService;
        this.otpService = otpService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/verify-email")
    public String verifyEmail(
            @Valid @RequestBody VerifyEmailRequest request) {

        return otpService.verifyEmail(request);

    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        LoginResponse response = refreshTokenService.refresh(request);

        return ResponseEntity.ok(response);
    }
}