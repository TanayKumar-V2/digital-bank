package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public String register(RegisterRequest request) {

        return "Registration Successful";

    }

}