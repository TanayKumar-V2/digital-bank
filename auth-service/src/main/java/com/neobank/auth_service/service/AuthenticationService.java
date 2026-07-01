package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.LoginRequest;
import com.neobank.auth_service.dto.LoginResponse;
import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.entity.enums.Role;
import com.neobank.auth_service.exception.EmailAlreadyExistsException;
import com.neobank.auth_service.exception.InvalidCredentialsException;
import com.neobank.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;
    private final JwtService jwtService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            System.out.println("Duplicate email detected");
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String hashedPassword = passwordEncoder.encode(
                request.getPassword());

        User user = new User();

        user.setEmail(normalizedEmail);
        user.setPassword(hashedPassword);
        user.setEmailVerified(false);
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        String otp = otpService.generateVerificationOtp(user);

        return "Registration Successful" + otp;

    }

    public LoginResponse login(LoginRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid email or password"));

        if (!user.getEmailVerified()) {
            throw new InvalidCredentialsException(
                    "Please verify your email first.");
        }

        boolean valid = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword());

        if (!valid) {
            throw new InvalidCredentialsException(
                    "Invalid email or password");
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(token, "Bearer");
    }
}