package com.neobank.auth_service.controller;

import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody RegisterRequest request) {

        return authenticationService.register(request);

    }

}