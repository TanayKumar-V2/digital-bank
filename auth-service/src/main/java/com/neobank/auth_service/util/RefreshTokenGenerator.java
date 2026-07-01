package com.neobank.auth_service.util;

import java.security.SecureRandom;
import java.util.Base64;

public final class RefreshTokenGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    private RefreshTokenGenerator() {
    }

    public static String generateRefreshToken() {

        byte[] bytes = new byte[32];

        secureRandom.nextBytes(bytes);

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}