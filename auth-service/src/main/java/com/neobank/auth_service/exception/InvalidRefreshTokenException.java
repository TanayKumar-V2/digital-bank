package com.neobank.auth_service.exception;

public class InvalidRefreshTokenException
        extends RuntimeException {

    public InvalidRefreshTokenException(
            String message) {

        super(message);

    }

}