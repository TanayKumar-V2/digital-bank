package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.RegisterRequest;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.exception.EmailAlreadyExistsException;
import com.neobank.auth_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(RegisterRequest request) {

        String normalizedEmail = request.getEmail()
                .trim()
                .toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent()) {
            System.out.println("Duplicate email detected");
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User(
                normalizedEmail,
                request.getPassword());

        System.out.println(normalizedEmail);
        userRepository.save(user);

        return "Registration Successful";

    }

}