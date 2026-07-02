package com.neobank.auth_service.exception;

public class CustomerAlreadyExistsException
        extends RuntimeException {

    public CustomerAlreadyExistsException(String message) {
        super(message);
    }
}