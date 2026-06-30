package com.neobank.auth_service.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.entity.VerificationToken;
import com.neobank.auth_service.entity.enums.VerificationType;
import com.neobank.auth_service.repository.VerificationTokenRepository;
import com.neobank.auth_service.util.OtpGenerator;

@Service
public class OtpService {
    private final VerificationTokenRepository verificationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    public OtpService(
            VerificationTokenRepository verificationTokenRepository,
            PasswordEncoder passwordEncoder) {
        this.verificationTokenRepository = verificationTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateVerificationOtp(User user) {
        String otp = OtpGenerator.generateOtp(6);
        String hashedOtp = passwordEncoder.encode(otp);
        VerificationToken token = new VerificationToken();
        token.setTokenHash(hashedOtp);
        token.setUser(user);
        token.setType(
                VerificationType.EMAIL_VERIFICATION);
        token.setExpiresAt(
                LocalDateTime.now().plusMinutes(5));
        verificationTokenRepository.save(token);
        return otp;
    }
}