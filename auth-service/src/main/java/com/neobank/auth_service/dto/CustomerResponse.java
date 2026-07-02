package com.neobank.auth_service.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CustomerResponse {

    private UUID id;

    private String email;

    private String fullName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String address;

    private LocalDateTime createdAt;

    public CustomerResponse() {
    }

    public CustomerResponse(
            UUID id,
            String email,
            String fullName,
            String phoneNumber,
            LocalDate dateOfBirth,
            String address,
            LocalDateTime createdAt) {

        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}