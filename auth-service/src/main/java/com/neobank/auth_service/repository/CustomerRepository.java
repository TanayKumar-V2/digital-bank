package com.neobank.auth_service.repository;

import com.neobank.auth_service.entity.Customer;
import com.neobank.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository
        extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByUser(User user);

}