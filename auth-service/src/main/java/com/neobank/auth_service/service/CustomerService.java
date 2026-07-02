package com.neobank.auth_service.service;

import com.neobank.auth_service.dto.CustomerRequest;
import com.neobank.auth_service.dto.CustomerResponse;
import com.neobank.auth_service.entity.Customer;
import com.neobank.auth_service.entity.User;
import com.neobank.auth_service.exception.CustomerAlreadyExistsException;
import com.neobank.auth_service.exception.UserNotFoundException;
import com.neobank.auth_service.repository.CustomerRepository;
import com.neobank.auth_service.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            UserRepository userRepository) {

        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CustomerResponse createProfile(CustomerRequest request) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        if (customerRepository.findByUser(user).isPresent()) {
            throw new CustomerAlreadyExistsException(
                    "Customer profile already exists");
        }

        Customer customer = new Customer();

        customer.setUser(user);
        customer.setFullName(request.getFullName());
        customer.setPhoneNumber(request.getPhoneNumber());
        customer.setDateOfBirth(request.getDateOfBirth());
        customer.setAddress(request.getAddress());

        customerRepository.save(customer);

        return new CustomerResponse(
                customer.getId(),
                user.getEmail(),
                customer.getFullName(),
                customer.getPhoneNumber(),
                customer.getDateOfBirth(),
                customer.getAddress(),
                customer.getCreatedAt()
        );
    }
}