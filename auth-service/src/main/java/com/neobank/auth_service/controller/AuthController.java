package com.neobank.auth_service.controller;

import com.neobank.auth_service.dto.LoginRequest;
import com.neobank.auth_service.dto.LoginResponse;
import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.dto.VerifyEmailRequest;
import com.neobank.auth_service.service.AuthenticationService;
import com.neobank.auth_service.service.OtpService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final OtpService otpService;

    public AuthController(
            AuthenticationService authenticationService,
            OtpService otpService) {

        this.authenticationService = authenticationService;
        this.otpService = otpService;
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
}