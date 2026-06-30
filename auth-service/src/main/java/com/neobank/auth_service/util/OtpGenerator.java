package com.neobank.auth_service.util;

import java.security.SecureRandom;

public class OtpGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateOtp(int length) {

        int min = (int) Math.pow(10, length - 1);

        int range = min * 9;

        int otp = min + secureRandom.nextInt(range);

        return String.valueOf(otp);
    }

}