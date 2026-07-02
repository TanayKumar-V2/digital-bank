package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.LoginResponse;
import com.neobank.auth_service.dto.RefreshTokenRequest;
import com.neobank.auth_service.entity.RefreshToken;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.exception.InvalidCredentialsException;
import com.neobank.auth_service.repository.RefreshTokenRepository;
import com.neobank.auth_service.util.RefreshTokenGenerator;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            JwtService jwtService) {

        this.refreshTokenRepository = refreshTokenRepository;
        this.jwtService = jwtService;
    }

    private RefreshToken getRefreshToken(String tokenHash) {

        return refreshTokenRepository
                .findByTokenHash(tokenHash)
                .orElseThrow(() -> new InvalidCredentialsException(
                        "Invalid refresh token"));
    }

    private boolean isExpired(RefreshToken refreshToken) {

        return refreshToken.getExpiresAt()
                .isBefore(LocalDateTime.now());

    }

    private void revokeToken(RefreshToken refreshToken) {

        refreshToken.setRevoked(true);

        refreshTokenRepository.save(refreshToken);

    }

    public String generateRefreshToken(User user) {

        String rawToken = RefreshTokenGenerator.generateRefreshToken();

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(user);
        refreshToken.setTokenHash(tokenHash);
        refreshToken.setExpiresAt(
                LocalDateTime.now().plusDays(30));

        refreshToken.setLastUsedAt(LocalDateTime.now());

        refreshTokenRepository.save(refreshToken);

        return rawToken;
    }

    private String hashToken(String token) {

        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(
                        String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(
                    "Unable to hash refresh token", e);

        }
    }

    public LoginResponse refresh(
            RefreshTokenRequest request) {

        String rawToken = request.getRefreshToken();

        String tokenHash = hashToken(rawToken);

        RefreshToken refreshToken = getRefreshToken(tokenHash);

        if (refreshToken.getRevoked()) {

            throw new InvalidCredentialsException(
                    "Refresh token has been revoked");

        }

        if (isExpired(refreshToken)) {

            throw new InvalidCredentialsException(
                    "Refresh token has expired");

        }

        User user = refreshToken.getUser();

        String accessToken = jwtService.generateToken(user);

        String newRefreshToken = generateRefreshToken(user);

        revokeToken(refreshToken);

        return new LoginResponse(
                accessToken,
                newRefreshToken,
                "Bearer");

    }
}