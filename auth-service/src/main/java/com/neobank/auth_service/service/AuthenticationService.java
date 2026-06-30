package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.exception.EmailAlreadyExistsException;
import com.neobank.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OtpService otpService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            OtpService otpService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.otpService = otpService;
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

        User user = new User(

                normalizedEmail,

                hashedPassword

        );
        System.out.println(normalizedEmail);
        userRepository.save(user);
        String otp = otpService.generateVerificationOtp(user);

        return "Registration Successful"+otp;

    }

}