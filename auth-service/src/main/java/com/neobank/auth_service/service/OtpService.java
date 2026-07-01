package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.VerifyEmailRequest;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.entity.VerificationToken;
import com.neobank.auth_service.entity.enums.VerificationType;
import com.neobank.auth_service.exception.InvalidOtpException;
import com.neobank.auth_service.exception.UserNotFoundException;
import com.neobank.auth_service.repository.UserRepository;
import com.neobank.auth_service.repository.VerificationTokenRepository;
import com.neobank.auth_service.util.OtpGenerator;

import jakarta.transaction.Transactional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OtpService {

    private final VerificationTokenRepository verificationTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public OtpService(
            VerificationTokenRepository verificationTokenRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.verificationTokenRepository = verificationTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateVerificationOtp(User user) {

        String otp = OtpGenerator.generateOtp(6);

        String hashedOtp = passwordEncoder.encode(otp);

        VerificationToken token = new VerificationToken();

        token.setUser(user);
        token.setTokenHash(hashedOtp);
        token.setType(VerificationType.EMAIL_VERIFICATION);
        token.setExpiresAt(LocalDateTime.now().plusMinutes(5));

        verificationTokenRepository.save(token);

        return otp;
    }

    @Transactional
    public String verifyEmail(VerifyEmailRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        VerificationToken token = verificationTokenRepository
                .findTopByUserAndTypeAndUsedFalseOrderByCreatedAtDesc(
                        user,
                        VerificationType.EMAIL_VERIFICATION)
                .orElseThrow(() ->
                        new InvalidOtpException("OTP not found"));

        if (LocalDateTime.now().isAfter(token.getExpiresAt())) {
            throw new InvalidOtpException("OTP has expired");
        }

        boolean valid = passwordEncoder.matches(
                request.getOtp(),
                token.getTokenHash());

        if (!valid) {
            throw new InvalidOtpException("Invalid OTP");
        }

        token.setUsed(true);

        user.setEmailVerified(true);


        return "Email verified successfully";
    }
}