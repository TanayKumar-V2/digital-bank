package com.neobank.auth_service.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import com.neobank.auth_service.entity.enums.Role;

@Entity
@Table(name = "users")
public class User {

    public User() {

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.emailVerified = false;
        this.role = Role.CUSTOMER;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean emailVerified;

    @PrePersist
    public void prePersist() {

        if (id == null) {
            id = UUID.randomUUID();
        }

        if (emailVerified == null) {
            emailVerified = false;
        }

    }
}